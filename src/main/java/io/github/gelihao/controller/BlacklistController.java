package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public CommonResult<Object> getList(@RequestParam Map listQuery){
        System.out.println("listQuery = " + listQuery);
        Long current = Long.parseLong((String) listQuery.get("current"));
        Long size = Long.parseLong((String) listQuery.get("size"));
        Page<Blacklist> page = new Page<>(current, size);
        Page<Blacklist> blacklistPage = blacklistService.page(page);
        Map<String,Object> data = new HashMap<>();
        data.put("total", blacklistPage.getTotal());
        data.put("items", blacklistPage.getRecords());

        return CommonResult.success(data);

    }

    @PostMapping("/delete")
    public CommonResult<Object> delete(@RequestBody String token){
//        System.out.println("token = " + token);
//        TODO 删除黑名单企业
        return CommonResult.success();

    }

//    @PostMapping("/delete")
//    public Result<String> getBlacklist(@RequestHeader("X-Token") String token, @RequestBody Blacklist blacklist) {
//        Claims claims = JWTUtil.parseToken(token, "BUAA");
//        Long uid = Long.parseLong(claims.getSubject());
//        System.out.println("请求网页的用户 uid = " + uid);
//        System.out.println("请求删除的黑名单 = " + blacklist);
//        boolean result = blacklistService.removeById(blacklist);
//        return result ? Result.success("删除黑名单成功"): Result.failed("删除黑名单失败");
//    }
//    @PostMapping("/add")
//    public Result<String> addBlacklist(@RequestBody Blacklist blacklist) {
//        System.out.println("请求添加的黑名单 = " + blacklist);
//        boolean save = blacklistService.save(blacklist);
//        if (save) {
//            return Result.success("添加黑名单成功");
//        }
//        else {
//            return Result.failed("添加黑名单失败");
//        }
//    }
}
