package io.github.gelihao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.entity.Examination;

import java.util.Map;


public interface ExaminationService extends IService<Examination> {

    Page<Examination> pageByListQuery(Map<String, String> listQuery);
}
