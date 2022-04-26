package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {

    @TableId
    private Integer id;
    private String identifier;
    private String companyname;
    private String evaluator;
    private Integer credit;
    private Integer status;
    private String remark;
    private Date editdate;
}
