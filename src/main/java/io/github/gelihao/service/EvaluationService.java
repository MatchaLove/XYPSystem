package io.github.gelihao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.gelihao.entity.Evaluation;

import java.util.Map;


public interface EvaluationService extends IService<Evaluation> {

    Page<Evaluation> pageByListQuery(Map<String, String> listQuery);
}
