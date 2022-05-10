package io.github.gelihao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.gelihao.entity.Blacklist;

import java.util.Map;


public interface BlacklistService extends IService<Blacklist> {
    Page<Blacklist> pageByListQuery(Map<String, String> listQuery);
}
