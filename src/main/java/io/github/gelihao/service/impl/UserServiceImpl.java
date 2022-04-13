package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.UserDao;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {


}
