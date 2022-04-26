package io.github.gelihao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.service.EvaluationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/creditGrading")
public class EvaluationController {

    @Resource
    private EvaluationService evaluationService;

    // http://localhost:8080/creditGrading/fetchlist?current=1&size=20
    @GetMapping("/fetchlist")
    public CommonResult<Object> getCredit(@RequestParam Map listQuery){
        System.out.println("listQuery = " + listQuery);
        Long current = Long.parseLong((String) listQuery.get("current"));
        Long size = Long.parseLong((String) listQuery.get("size"));
//        System.out.println("size = " + size);
//        System.out.println("current = " + current);
        Page<Evaluation> page = new Page<>(current, size);
        Page<Evaluation> evaluationPage = evaluationService.page(page);
        Map<String,Object> data = new HashMap<>();
        data.put("total", evaluationPage.getTotal());
        data.put("items", evaluationPage.getRecords());
        return CommonResult.success(data);//data

    }


}
