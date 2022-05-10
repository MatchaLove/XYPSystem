package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao extends BaseMapper<User> {
    @Select("SELECT xyp_user.company " +
            " FROM xyp_user " +
            " WHERE xyp_user.username = #{username} " )
    String findIdentifierByUsername(@Param("username") String username);
}
