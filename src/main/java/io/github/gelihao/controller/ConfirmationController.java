package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.ConfirmationService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/confirmation")
public class ConfirmationController {

    @Resource
    private ConfirmationService confirmationService;
    @Resource
    private UserService userService;


    @PostMapping(value = "/submit")
    public CommonResult<Object> uploadFile(@RequestParam("files") MultipartFile[] files, @RequestParam("username") String username) throws IOException {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        User one = userService.getOne(queryWrapper);

        String identifier = one.getCompany();
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

            filenames.append(filePath).append(";");
            // 4. 保存文件信息到数据库
            // 4.1 搞个实体类，把你需要的文件信息保存到实体类中
            // 4.2 调用Service层或者Dao层，保存数据库即可。

        }
        System.out.println("文件"+filenames);
        QueryWrapper<Confirmation> conqueryWrapper = new QueryWrapper();
        conqueryWrapper.eq("identifier",identifier);
        Confirmation confirmationOne = confirmationService.getOne(conqueryWrapper);

        if (Objects.isNull(confirmationOne)){
            Confirmation confirmation = new Confirmation();
            confirmation.setIdentifier(identifier);
            confirmation.setFilepath(String.valueOf(filenames));
            confirmation.setEditdate(new Date());
            confirmation.setFilepath(String.valueOf(filenames));
            confirmationService.save(confirmation);
        }else{
            String oriFilePath = confirmationOne.getFilepath();
            confirmationOne.setFilepath(oriFilePath + filenames);
            confirmationOne.setEditdate(new Date());
            confirmationService.saveOrUpdate(confirmationOne);
            System.out.println(oriFilePath + filenames);
        }


        return CommonResult.success(data);
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
