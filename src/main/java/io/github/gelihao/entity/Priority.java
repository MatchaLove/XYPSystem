package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Priority {

    @TableId
    private Integer id;
    private String identifier;
    private String companyname;
    private Integer priority;
    private Date editdate;
}
