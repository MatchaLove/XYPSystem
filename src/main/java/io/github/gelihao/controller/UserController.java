package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.*;
import io.github.gelihao.service.CompanyService;
import io.github.gelihao.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private CompanyService companyService;

    @PostMapping ("/login")
    public CommonResult<Map<String,String>> login(@RequestBody User user){
        //System.out.println(user);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",user.getUsername()).eq("password",user.getPassword());
        User one = userService.getOne(queryWrapper);
        if (Objects.isNull(one)){
            return CommonResult.failed("用户名或密码错误");
        }else{
            Map<String,String> data = new HashMap<>();
            // TODO 根据用户信息生成token，并且将用户信息存入缓存
            //需更改前端的验证逻辑
            data.put("token", one.getUsername());
            return CommonResult.success(data);
        }
    }

    //https://avatars.githubusercontent.com/u/62493195?v=4
    @GetMapping("/info")
    public CommonResult<Map<String,Object>> getInfo(@RequestParam("token") String token){
        //System.out.println("params" + token);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",token);
        User one = userService.getOne(queryWrapper);
        if (Objects.isNull(one)){
            return CommonResult.failed("用户登录信息过期");
        }else{
            Map<String,Object> data = new HashMap<>();
            data.put("name",one.getNickname());
            data.put("username",one.getUsername());
            data.put("avatar",one.getAvatar());
            List<String> roles = new ArrayList<>();
            roles.add(one.getRole());
            data.put("roles",roles);
            data.put("information","未创建information字段");
            return CommonResult.success(data);
        }
    }

    @GetMapping("/getcompany")
    public CommonResult<Map<String,Object>> getCompanyInfo(@RequestParam("username") String username){
        //System.out.println("params" + token);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);

        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper();
        companyQueryWrapper.eq("identifier",user.getCompany());
        Company company = companyService.getOne(companyQueryWrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("identifier",company.getIdentifier());
        data.put("companyname",company.getCompanyname());
        data.put("chargeman",company.getChargeman());
        data.put("address",company.getAddress());
        data.put("email", company.getEmail());
        data.put("phone",company.getPhone());

        return CommonResult.success(data);

    }
    @PostMapping(value = "/submit")
    public CommonResult<Object> uploadFile(@RequestParam("files") MultipartFile[] files,
                                           HttpServletRequest request) throws Exception {
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
        System.out.println(parmMap);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",parmMap.get("username"));
        User userOne = userService.getOne(queryWrapper);
//        String identifier = one.getCompany();
        parmMap.remove("username");

        ObjectMapper objectMapper = new ObjectMapper();
        Company company = objectMapper.convertValue(parmMap, Company.class);
//        company.setIdentifier(identifier);

        StringBuilder filenames = new StringBuilder();
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + filename;
            // 3. 接下来调用方法来保存文件到本地磁盘,返回的是保存后的文件路径
            String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
            filenames.append(filePath);
        }
        userOne.setQualification(String.valueOf(filenames));
        userOne.setIfqualification(1);
//        userOne.se(new Date());
        userService.update(userOne,queryWrapper);
        // TODO 插入待评价企业
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper();
        companyQueryWrapper.eq("identifier",parmMap.get("identifier"));
        Company companyOne = companyService.getOne(companyQueryWrapper);
        if (Objects.isNull(companyOne)){
            companyService.save(company);
        }else{
            companyService.update(company,companyQueryWrapper);
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

    @PostMapping("/logout")
    public CommonResult<Object> logout(@RequestBody String token){
//        System.out.println("token = " + token);
//        TODO 清除用户缓存
        return CommonResult.success();

    }
    @GetMapping(value = "/download")
    public void downloadFile(
            @RequestParam("username") String username,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        // 下载文件
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.pdf");
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        User userOne = userService.getOne(queryWrapper);
        String filePath = userOne.getQualification();
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
    @PostMapping("/updateuser")
    public CommonResult<Object> updateUser(@RequestBody User user){
        System.out.println(user);

        userService.updateById(user);
        return CommonResult.success();

    }

    @GetMapping("/fetchlist")
    public CommonResult<Object> getUserList(@RequestParam Map listQuery){

        Page<User> userPage = userService.pageByListQuery(listQuery);
        Map<String,Object> data = new HashMap<>();
        data.put("total", userPage.getTotal());
        data.put("items", userPage.getRecords());
        return CommonResult.success(data);//data

    }

    @PostMapping("/adduser")
    public CommonResult<Object> addUser(@RequestBody User user){
        System.out.println(user);
        userService.save(user);
        return CommonResult.success();

    }
    @PostMapping ("/signup")
    public CommonResult<Map<String,String>> signUp(@RequestBody User user){
        //System.out.println(user);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",user.getUsername()).eq("password",user.getPassword());
        User one = userService.getOne(queryWrapper);
        if (Objects.isNull(one)){
            return CommonResult.failed("用户名或密码错误");
        }else{
            Map<String,String> data = new HashMap<>();
            // TODO 根据用户信息生成token，并且将用户信息存入缓存
            //需更改前端的验证逻辑
            data.put("token", one.getUsername());
            return CommonResult.success(data);
        }
    }
}
