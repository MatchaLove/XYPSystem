package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.dao.MaterialDao;
import io.github.gelihao.entity.Company;
import io.github.gelihao.entity.Material;
import io.github.gelihao.service.CompanyService;
import io.github.gelihao.service.MaterialService;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialDao, Material> implements MaterialService {


}
