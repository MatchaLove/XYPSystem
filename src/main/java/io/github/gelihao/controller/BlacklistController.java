package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.service.BlacklistService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

    @Resource
    private BlacklistService blacklistService;


//    @PostMapping ("/editlist")
//    public CommonResult<Map<String,String>> login(@RequestBody User user){
//        //System.out.println(user);
//        QueryWrapper<User> queryWrapper = new QueryWrapper();
//        queryWrapper.eq("username",user.getUsername()).eq("password",user.getPassword());
//        User one = userService.getOne(queryWrapper);
//        if (Objects.isNull(one)){
//            return CommonResult.failed("用户名或密码错误");
//        }else{
//            Map<String,String> data = new HashMap<>();
//
//            //需更改前端的验证逻辑
//            data.put("token","admin");
//            return CommonResult.success(data);
//        }
//    }
    //https://avatars.githubusercontent.com/u/62493195?v=4
    @GetMapping("/fetchlist")
    public CommonResult<Object> getList(){
        Map<String,Object> data = new HashMap<>();
        //System.out.println("params" + token);
        List<Blacklist> blacklists = blacklistService.list();

        data.put("total", blacklists.size());
        data.put("items", blacklists);

        return CommonResult.success(data);

    }

    @PostMapping("/delete")
    public CommonResult<Object> delete(@RequestBody String token){
//        System.out.println("token = " + token);
//        TODO 删除黑名单企业
        return CommonResult.success();

    }
}
