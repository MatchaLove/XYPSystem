package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.BlacklistDao;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.service.BlacklistService;
import org.springframework.stereotype.Service;

@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistDao, Blacklist> implements BlacklistService {


}
