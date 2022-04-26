package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompanyDao extends BaseMapper<Company> {

    @Select("SELECT xyp_company.credit " +
            " FROM xyp_user, xyp_company " +
            " WHERE xyp_user.username = #{username} " +
            "     AND xyp_company.identifier = xyp_user.company")
    Integer findUserCreditByUsername(@Param("username") String username);

}
