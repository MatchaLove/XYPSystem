package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.ExaminationDao;
import io.github.gelihao.entity.Examination;
import io.github.gelihao.service.ExaminationService;
import org.springframework.stereotype.Service;

@Service
public class ExaminationServiceImpl extends ServiceImpl<ExaminationDao, Examination> implements ExaminationService {


}
