package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.Application;
import io.github.gelihao.entity.Confirmation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationDao extends BaseMapper<Application> {
}
