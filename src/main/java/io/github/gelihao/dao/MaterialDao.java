package io.github.gelihao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.gelihao.entity.Material;
import io.github.gelihao.entity.Priority;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaterialDao extends BaseMapper<Material> {
}
