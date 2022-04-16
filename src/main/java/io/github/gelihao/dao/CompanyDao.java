package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.Company;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyDao extends BaseMapper<Company> {
}
