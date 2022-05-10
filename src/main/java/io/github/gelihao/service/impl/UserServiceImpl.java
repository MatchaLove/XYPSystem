package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.EvaluationDao;
import io.github.gelihao.dao.UserDao;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public Page<User> pageByListQuery(Map<String, String> listQuery) {
        System.out.println("listQuery = " + listQuery);
        String username = listQuery.get("username");
        String nickname = listQuery.get("nickname");
        String role = listQuery.get("role");
        Page<User> page = new Page<>(
                Long.parseLong(listQuery.get("current")),
                Long.parseLong(listQuery.get("size"))
        );
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.like(StringUtils.isNotBlank(nickname), "nickname", nickname);
        queryWrapper.like(StringUtils.isNotBlank(role), "role", role);
        return userDao.selectPage(page, queryWrapper);
    }

}
