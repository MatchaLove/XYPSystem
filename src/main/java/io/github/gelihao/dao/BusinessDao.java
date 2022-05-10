package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.Business;
import io.github.gelihao.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BusinessDao extends BaseMapper<Business> {


    @Select("SELECT xyp_business.businessname " +
            " FROM xyp_business " +
            " WHERE xyp_business.businessid = #{businessid} " )
    String findNameById(@Param("businessid") String businessid);
}
