package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
