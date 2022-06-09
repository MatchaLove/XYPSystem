package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.BusinessDao;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.entity.Business;
import io.github.gelihao.entity.Company;
import io.github.gelihao.service.BusinessService;
import io.github.gelihao.service.CompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessDao, Business> implements BusinessService {

    @Resource
    private BusinessDao businessDao;
    @Override
    public Page<Business> pageByListQuery(Map<String, String> listQuery) {
        System.out.println("listQuery = " + listQuery);
        String businessname = listQuery.get("businessname");
        String businessid = listQuery.get("businessid");
        Page<Business> page = new Page<>(
                Long.parseLong(listQuery.get("current")),
                Long.parseLong(listQuery.get("size"))
        );
        QueryWrapper<Business> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(businessname), "businessname", businessname);
        queryWrapper.like(StringUtils.isNotBlank(businessid), "businessid", businessid);
        return businessDao.selectPage(page, queryWrapper);
    }

}
