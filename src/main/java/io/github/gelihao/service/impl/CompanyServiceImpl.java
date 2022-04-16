package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Company;
import io.github.gelihao.service.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyDao, Company> implements CompanyService {


}
