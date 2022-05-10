package io.github.gelihao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.gelihao.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Page<User> pageByListQuery(Map<String, String> listQuery);
}
