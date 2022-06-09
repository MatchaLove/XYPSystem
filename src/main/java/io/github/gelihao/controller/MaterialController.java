package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.Application;
import io.github.gelihao.entity.Business;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.entity.Material;
import io.github.gelihao.service.BusinessService;
import io.github.gelihao.service.MaterialService;
import io.github.gelihao.utils.ZipUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Resource
    private MaterialService materialService;

    @GetMapping(value = "/download")
    public void downloadFile(
            @RequestParam("id") Integer id,
            @RequestParam("type") Integer type,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        // type 0emptyfile 1demofile

        QueryWrapper<Material> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        Material one = materialService.getOne(queryWrapper);
        List<File> fileList = new ArrayList<>();
        if (type == 0){
            String filePath = one.getEmptyfile();
            fileList.add(new File(filePath));
        }else if(type == 1){
            String filePath = one.getDemofile();
            fileList.add(new File(filePath));
        }else{
            System.out.println("错误的文件请求");
        }

        String fileName = one.getMaterialname();
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

    }

}
