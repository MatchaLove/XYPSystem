package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.ConfirmationService;
import io.github.gelihao.service.EvaluationService;
import io.github.gelihao.utils.ZipUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@RestController
@RequestMapping("/creditGrading")
public class EvaluationController {

    @Resource
    private EvaluationService evaluationService;
    @Resource
    private ConfirmationService confirmationService;


    // http://localhost:8080/creditGrading/fetchlist?current=1&size=20
    @GetMapping("/fetchlist")
    public CommonResult<Object> getEvaluationList(@RequestParam Map listQuery){
        Long current = Long.parseLong((String) listQuery.get("current"));
        Long size = Long.parseLong((String) listQuery.get("size"));

        Page<Evaluation> evaluationPage = evaluationService.pageByListQuery(listQuery);

        Map<String,Object> data = new HashMap<>();
        data.put("total", evaluationPage.getTotal());
        data.put("items", evaluationPage.getRecords());
        return CommonResult.success(data);
    }
    @PostMapping ("/updatecredit")
    public CommonResult<Object> updateCredit(@RequestBody Evaluation evaluation){
        System.out.println(evaluation);

        evaluationService.updateById(evaluation);
        // TODO 合约调用
        return CommonResult.success();

    }

    @GetMapping("/getdata")
    public CommonResult<Object> getData(@RequestParam("identifier") String identifier){
        QueryWrapper<Confirmation> confirmationQueryWrapper = new QueryWrapper();
        confirmationQueryWrapper.eq("identifier",identifier);
        Confirmation one = confirmationService.getOne(confirmationQueryWrapper);

        BeanMap data = BeanMap.create(one);

        return CommonResult.success(data);
    }

    @GetMapping(value = "/download")
    public void downloadFile(
            @RequestParam("identifier") String identifier,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        // 3. 下载文件
        response.setHeader("content-type", "application/octet-stream");
//        response.setHeader("content-type", "application/pdf");
//        response.setContentType("application/octet-stream");
//        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.pdf");

        QueryWrapper<Confirmation> queryWrapper = new QueryWrapper();
        queryWrapper.eq("identifier", identifier);
        Confirmation one = confirmationService.getOne(queryWrapper);
        String filePath = one.getFilepath();
        File file =new File(filePath);
        FileInputStream in = new FileInputStream(file);
//        ByteArrayInputStream in =new ByteArrayInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] bytes =new byte[BUFFER_SIZE];
        while((in.read(bytes))!=-1){
            out.write(bytes);
        }
        out.flush();
        in.close();
        out.close();

//        ZipUtils.downloadZip(response.getOutputStream(), fileList);

    }


}
