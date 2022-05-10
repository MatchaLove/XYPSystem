package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.ConfirmationService;
import io.github.gelihao.service.EvaluationService;
import io.github.gelihao.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/confirmation")
public class ConfirmationController {

    @Resource
    private ConfirmationService confirmationService;
    @Resource
    private UserService userService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private CompanyDao companyDao;

    @PostMapping(value = "/submit")
    public CommonResult<Object> uploadFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) throws Exception {
        Map<String,String> parmMap=new HashMap<String,String>();
        // getParameterMap()，获得请求参数map
        Map<String,String[]> map= request.getParameterMap();
        //参数名称
        Set<String> key=map.keySet();
        //参数迭代器
        Iterator<String> iterator = key.iterator();
        while(iterator.hasNext()){
            String k=iterator.next();
            parmMap.put(k, map.get(k)[0]);

        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",parmMap.get("username"));
        User one = userService.getOne(queryWrapper);
        String identifier = one.getCompany();
        parmMap.remove("username");

        ObjectMapper objectMapper = new ObjectMapper();
        Confirmation confirmation = objectMapper.convertValue(parmMap, Confirmation.class);
        confirmation.setIdentifier(identifier);

        StringBuilder filenames = new StringBuilder();
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + filename;
            // 3. 接下来调用方法来保存文件到本地磁盘,返回的是保存后的文件路径
            String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
            filenames.append(filePath);
        }
        confirmation.setFilepath(String.valueOf(filenames));
        confirmation.setEditdate(new Date());
        confirmationService.save(confirmation);
        // TODO 插入待评价企业
        QueryWrapper<Evaluation> evaluationQueryWrapper = new QueryWrapper();
        evaluationQueryWrapper.eq("identifier",identifier);
        Evaluation evaluationOne = evaluationService.getOne(evaluationQueryWrapper);
        if (Objects.isNull(evaluationOne)){

            Evaluation evaluation = new Evaluation();
            evaluation.setIdentifier(identifier);
            evaluation.setCompanyname(companyDao.findNameByIdentifier(identifier));
            evaluation.setEditdate(new Date());
            evaluationService.save(evaluation);
        }else{
//            evaluationOne.setEditdate(new Date());
//            evaluationOne.setEvaluator("");
//            evaluationOne.setStatus(0);
//            evaluationOne.setCredit(0);
            return CommonResult.failed("您已进行评价，请勿重复申请");
        }
        return CommonResult.success("申请成功");
    }

    public static String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 项目路径/FileSpace  也就是和src同级
        String fileSpace = System.getProperty("user.dir") + File.separator + "FileSpace";
        String path = fileSpace + File.separator + fileName;
        // 判断父文件夹是否存在
        File file = new File(path);
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        // 通过NIO保存文件到本地磁盘
        try {
            FileOutputStream fos = new FileOutputStream(path);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public void mapToEntity(Object entity,Map<String, String> paramsMap) throws Exception {
        Field[] tenderProjectFields = entity.getClass().getDeclaredFields();
        for (int i = 0; i < tenderProjectFields.length; i++) {
            String fieldName = tenderProjectFields[i].getName();
            String paramsMapVlue = paramsMap.get(fieldName);
            if (StringUtils.isEmpty(paramsMapVlue)) {
                continue;
            }
            tenderProjectFields[i].setAccessible(true);
            tenderProjectFields[i].set(entity, paramsMapVlue);
            //System.out.println(fieldName + " = " + paramsMapVlue);
        }
    }

}
