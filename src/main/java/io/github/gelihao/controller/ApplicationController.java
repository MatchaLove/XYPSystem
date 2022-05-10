package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.dao.BusinessDao;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.dao.UserDao;
import io.github.gelihao.entity.Application;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.entity.Examination;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.ApplicationService;
import io.github.gelihao.service.ConfirmationService;
import io.github.gelihao.service.ExaminationService;
import io.github.gelihao.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;
    @Resource
    private ExaminationService examinationService;
    @Resource
    private BusinessDao businessDao;
    @Resource
    private CompanyDao companyDao;
    @Resource
    private UserDao userDao;


    @PostMapping(value = "/submit")
    public CommonResult<Object> uploadFile(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("identifier") String identifier,
            @RequestParam("businessid") String businessid
            ) throws IOException {

        Map<String,Object> data = new HashMap<>();
        StringBuilder filenames = new StringBuilder();
        // 1. 用数组MultipartFile[]来表示多文件,所以遍历数组,对其中的文件进行逐一操作
        for (MultipartFile file : files) {
            // 2. 通过一顿file.getXXX()的操作,获取文件信息。
            // 2.1 这里用文件名举个栗子
            String filename = file.getOriginalFilename();
            filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + filename;
            // 3. 接下来调用方法来保存文件到本地磁盘,返回的是保存后的文件路径
            String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
            filenames.append(filePath);
        }
//        QueryWrapper<Application> conqueryWrapper = new QueryWrapper();
//        conqueryWrapper.eq("identifier",identifier);
//        Application applicationOne = applicationService.getOne(conqueryWrapper);

//        if (Objects.isNull(confirmationOne)){
        Application application = new Application();
        application.setIdentifier(identifier);
        application.setBusinessid(businessid);
        application.setFilepath(String.valueOf(filenames));
        application.setEditdate(new Date());
        applicationService.save(application);
//        }else{
//            String oriFilePath = confirmationOne.getFilepath();
//            confirmationOne.setFilepath(oriFilePath + filenames);
//            confirmationOne.setEditdate(new Date());
//            confirmationService.saveOrUpdate(confirmationOne);
//            System.out.println(oriFilePath + filenames);
//        }
        QueryWrapper<Examination> examinationQueryWrapper = new QueryWrapper();
        examinationQueryWrapper.eq("identifier",identifier).eq("businessid",businessid);
        Examination examinationOne = examinationService.getOne(examinationQueryWrapper);
        if (Objects.isNull(examinationOne)){
            Examination examination = new Examination();
            examination.setIdentifier(identifier);
            examination.setCompanyname(companyDao.findNameByIdentifier(identifier));
            examination.setBusinessid(businessid);
            examination.setBusinessname(businessDao.findNameById(businessid));
            examination.setStatus(0);
            examination.setEditdate(new Date());
            examinationService.save(examination);
        }else{
            return CommonResult.failed("请勿重复申请");
        }

        return CommonResult.success(data);
    }

    @GetMapping("/getprogress")
    public CommonResult<Object> getProgress(@RequestParam("username") String username){

        String identifier = userDao.findIdentifierByUsername(username);

        List<Map> data = new ArrayList<>();

        QueryWrapper<Application> queryWrapper = new QueryWrapper();
        queryWrapper.eq("identifier", identifier);
        List<Application> applications = applicationService.list(queryWrapper);
        if (Objects.isNull(applications)) {
            Map<String,Object> activity = new HashMap<>();
            activity.put("title", "未申请");
            activity.put("icon", "el-icon-more");
            data.add(activity);
            return CommonResult.success(data);
        }else{
            for (Application application : applications) {
                Map<String,Object> activity = new HashMap<>();
                String businessid = application.getBusinessid();
                Date editdate = application.getEditdate();
                activity.put("title", "提交申请");
                activity.put("content", "您于" + editdate + "提交" + businessDao.findNameById(businessid) + "业务申请");
                data.add(activity);

                QueryWrapper<Examination> examinationQueryWrapper = new QueryWrapper();
                examinationQueryWrapper.eq("identifier", identifier).eq("businessid", businessid);
                Examination one = examinationService.getOne(examinationQueryWrapper);
                Map<String,Object> exmAct = new HashMap<>();
                if (one.getStatus() == 0){
                    exmAct.put("title", "尚未审批");
                    exmAct.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请尚未审批");
                    exmAct.put("type", "warning");
                    data.add(exmAct);
                }else if (one.getStatus() == 1){
                    exmAct.put("title", "审批通过");
                    exmAct.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请审批通过");
                    exmAct.put("type", "success");
                    data.add(exmAct);
                }else if (one.getStatus() == 2){
                    exmAct.put("title", "审批未通过");
                    exmAct.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请审批未通过");
                    exmAct.put("type", "danger");
                    data.add(exmAct);
                }

            }
        }


        return CommonResult.success(data);//data

    }
    @GetMapping("/getInfo")
    public CommonResult<Object> getInfo(@RequestParam("username") String username){

        String identifier = userDao.findIdentifierByUsername(username);
        String companyname = companyDao.findNameByIdentifier(identifier);

        Map<String,Object> data = new HashMap<>();
        data.put("identifier", identifier);
        data.put("companyname", companyname);
        return CommonResult.success(data);//data

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


}
