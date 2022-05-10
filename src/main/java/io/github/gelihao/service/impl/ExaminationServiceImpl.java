package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.EvaluationDao;
import io.github.gelihao.dao.ExaminationDao;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.entity.Examination;
import io.github.gelihao.service.ExaminationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ExaminationServiceImpl extends ServiceImpl<ExaminationDao, Examination> implements ExaminationService {
    @Resource
    private ExaminationDao examinationDao;

//    TODO 根据字段更改
    @Override
    public Page<Examination> pageByListQuery(Map<String, String> listQuery) {
        System.out.println("listQuery = " + listQuery);
        String companyname = listQuery.get("companyname");
        String identifier = listQuery.get("identifier");
        String status = listQuery.get("status");
        Page<Examination> page = new Page<>(
                Long.parseLong(listQuery.get("current")),
                Long.parseLong(listQuery.get("size"))
        );
        QueryWrapper<Examination> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(companyname), "companyname", companyname);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(status), "status", status);
        return examinationDao.selectPage(page, queryWrapper);
    }

}
