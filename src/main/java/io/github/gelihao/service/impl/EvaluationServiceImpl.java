package io.github.gelihao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.gelihao.dao.EvaluationDao;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.service.EvaluationService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationDao, Evaluation> implements EvaluationService {


}
