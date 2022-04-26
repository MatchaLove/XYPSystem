package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.Company;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.CompanyService;
import io.github.gelihao.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/credit")
public class FiscoCreditController {
    @Resource
    private UserService userService;


    @GetMapping("/creditInquiry1")
    public CommonResult<Object> getCredit(@RequestParam String username){
        Map<String,Object> data = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        User one = userService.getOne(queryWrapper);
        //System.out.println("params" + token);
        return CommonResult.success();

    }


}
