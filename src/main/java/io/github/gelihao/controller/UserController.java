package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


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
            data.put("token","admin");
            return CommonResult.success(data);
        }
    }
    //https://avatars.githubusercontent.com/u/62493195?v=4
    @GetMapping("/info")
    public CommonResult<Map<String,String>> getInfo(@RequestParam("token") String token){
        //System.out.println("params" + token);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",token);
        User one = userService.getOne(queryWrapper);
        if (Objects.isNull(one)){
            return CommonResult.failed("用户登录信息过期");
        }else{
            Map<String,String> data = new HashMap<>();
            data.put("name",one.getNickname());
            data.put("avatar",one.getAvatar());
            return CommonResult.success(data);
        }
    }

    @PostMapping("/logout")
    public CommonResult<Object> logout(@RequestBody String token){
//        System.out.println("token = " + token);
//        TODO 清除用户缓存
        return CommonResult.success();

    }
}
