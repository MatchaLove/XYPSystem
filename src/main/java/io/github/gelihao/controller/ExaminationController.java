package io.github.gelihao.controller;

import io.github.gelihao.api.CommonResult;
import io.github.gelihao.dao.CompanyDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/rongque")
public class ExaminationController {

    @Resource
    private CompanyDao companyDao;

    // 材料审核页面
    @GetMapping("/materialAudit")
    public CommonResult<Object> getCredit(@RequestParam("username") String username){
//        System.out.println("usernameresult" + username);
//        Integer credit = companyDao.findUserCreditByUsername(username);
//        Map<String,Integer> data = new HashMap<>();
//        data.put("credit", credit);
//        System.out.println("creditresult" + credit);
        return CommonResult.success();//data

    }


}
