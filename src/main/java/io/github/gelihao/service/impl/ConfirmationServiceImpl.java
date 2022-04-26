package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.ConfirmationDao;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.service.ConfirmationService;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationServiceImpl extends ServiceImpl<ConfirmationDao, Confirmation> implements ConfirmationService {


}
