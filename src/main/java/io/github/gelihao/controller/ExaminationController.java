package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Application;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.entity.Examination;
import io.github.gelihao.service.ApplicationService;
import io.github.gelihao.service.ExaminationService;
import io.github.gelihao.utils.ZipUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/examination")
public class ExaminationController {

    @Resource
    private ExaminationService examinationService;
    @Resource
    private ApplicationService applicationService;

    // 材料审核页面
    @GetMapping("/fetchlist")
    public CommonResult<Object> getExaminationList(@RequestParam Map listQuery){

        Page<Examination> examinationPage = examinationService.pageByListQuery(listQuery);
        Map<String,Object> data = new HashMap<>();
        data.put("total", examinationPage.getTotal());
        data.put("items", examinationPage.getRecords());
        return CommonResult.success(data);//data

    }
    @PostMapping("/updateExaminate")
    public CommonResult<Object> updateExaminate(@RequestBody Examination examination){
        System.out.println(examination);

        examinationService.updateById(examination);
        return CommonResult.success();

    }
    @GetMapping(value = "/download")
    public void downloadFile(
            @RequestParam("identifier") String identifier,
            @RequestParam("businessid") String businessid,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        // TODO 文件下载逻辑
        // 如果你想根据其他方式来下载指定文件的话，请自己修改业务逻辑
        // 1. 根据fileId从数据库中获取到指定的文件信息，包括文件名、文件存储地址等等。
        // 1.1 假设我已经获取到了文件信息。
        QueryWrapper<Application> queryWrapper = new QueryWrapper();
        queryWrapper.eq("identifier", identifier).eq("businessid", businessid);
        List<Application> list = applicationService.list(queryWrapper);
//        List<Confirmation> list = confirmationService.list(queryWrapper);
//        for (Confirmation confirmation:list){
//
//        }
        List<File> fileList = new ArrayList<>();
        for (Application application:list){
            String filePath = application.getFilepath();
            fileList.add(new File(filePath));
        }
        String fileName = identifier;


        // 2. 解决下载的文件的文件名出现中文乱码
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            // IE浏览器
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        // 3. 下载文件

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.zip");
        ZipUtils.downloadZip(response.getOutputStream(), fileList);
      /*  byte[] data = Files.readAllBytes(Paths.get(filePath));
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(data.length)
                .body(resource);*/
    }

}
