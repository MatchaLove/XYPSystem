package io.github.gelihao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.entity.Business;
import io.github.gelihao.entity.Company;

import java.util.Map;


public interface BusinessService extends IService<Business> {
    Page<Business> pageByListQuery(Map<String, String> listQuery);
}
