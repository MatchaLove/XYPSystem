package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Company;
import io.github.gelihao.entity.Priority;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/credit")
public class CompanyController {

    @Resource
    private CompanyDao companyDao;

    @GetMapping("/creditInquiry")
    public CommonResult<Object> getCredit(@RequestParam("username") String username){
        System.out.println("usernameresult" + username);
        Integer credit = companyDao.findUserCreditByUsername(username);
        Map<String,Integer> data = new HashMap<>();
        data.put("credit", credit);
        System.out.println("creditresult" + credit);
        return CommonResult.success(data);

    }

    @PostMapping("/delete")
    public CommonResult<Object> delete(@RequestBody String token){
//        System.out.println("token = " + token);
//        TODO 删除黑名单企业
        return CommonResult.success();

    }
}
