package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {

    @TableId
    private Integer id;
    private String businessid;
    private String businessname;
    private Integer mincredit;
    private Integer type;
    private String process;
    private String mainfile;
    private String secondfile;


}
