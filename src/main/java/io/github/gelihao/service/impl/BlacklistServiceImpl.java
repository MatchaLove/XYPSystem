package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.BlacklistDao;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.service.BlacklistService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistDao, Blacklist> implements BlacklistService {
    @Resource
    private BlacklistDao blacklistDao;

    @Override
    public Page<Blacklist> pageByListQuery(Map<String, String> listQuery) {
        System.out.println("listQuery = " + listQuery);
        String companyname = listQuery.get("companyname");
        String identifier = listQuery.get("identifier");
        Page<Blacklist> page = new Page<>(
                Long.parseLong(listQuery.get("current")),
                Long.parseLong(listQuery.get("size"))
        );
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(companyname), "companyname", companyname);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "identifier", identifier);
        return blacklistDao.selectPage(page, queryWrapper);
    }

}
