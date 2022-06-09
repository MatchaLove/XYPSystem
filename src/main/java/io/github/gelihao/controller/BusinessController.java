package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.*;
import io.github.gelihao.service.BusinessService;
import io.github.gelihao.service.ExaminationService;
import io.github.gelihao.service.PriorityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;
    @Resource
    private ExaminationService examinationService;



    @GetMapping("/fetchlist")
    public CommonResult<Object> getList(@RequestParam Map listQuery){
        Map<String,Object> data = new HashMap<>();
        Page<Business> businessPage = businessService.pageByListQuery(listQuery);


        data.put("total", businessPage.getTotal());
        data.put("items", businessPage.getRecords());

        return CommonResult.success(data);

    }
    @GetMapping("/getmincredit")
    public CommonResult<Object> getMinCredit(@RequestParam("businessid") String businessid,@RequestParam("identifier") String identifier){
        QueryWrapper<Business> queryWrapper = new QueryWrapper();
        queryWrapper.eq("businessid",businessid);
        Business one = businessService.getOne(queryWrapper);
        QueryWrapper<Examination> examinationQueryWrapper = new QueryWrapper();
        examinationQueryWrapper.eq("identifier",identifier).eq("businessid",businessid);
        Examination examinationOne = examinationService.getOne(examinationQueryWrapper);
        Map<String,Object> data = new HashMap<>();
        data.put("mincredit", one.getMincredit());
        if (Objects.isNull(examinationOne)){
            data.put("appexist", 0);//不存在
        }else{
            data.put("appexist", 1);
        }
        return CommonResult.success(data);

    }


}
