package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.Blacklist;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlacklistDao extends BaseMapper<Blacklist> {
}
