package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.BlacklistDao;
import io.github.gelihao.dao.EvaluationDao;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.service.EvaluationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationDao, Evaluation> implements EvaluationService {
    @Resource
    private EvaluationDao evaluationDao;

    @Override
    public Page<Evaluation> pageByListQuery(Map<String, String> listQuery) {
        System.out.println("listQuery = " + listQuery);
        String companyname = listQuery.get("companyname");
        String identifier = listQuery.get("identifier");
        String status = listQuery.get("status");
        Page<Evaluation> page = new Page<>(
                Long.parseLong(listQuery.get("current")),
                Long.parseLong(listQuery.get("size"))
        );
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(companyname), "companyname", companyname);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(status), "status", status);
        return evaluationDao.selectPage(page, queryWrapper);
    }

}
