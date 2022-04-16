package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist {

    @TableId
    private Integer id;
    private String identifier;
    private String companyname;
    private String reason;
    private Date joindate;
}
