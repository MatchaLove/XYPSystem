package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @TableId
    private Integer id;
    private String businessid;
    private String materialname;
    private String demofile;
    private Integer style;
    private Integer ifdemofile;
    private Integer ifemptyfile;
    private Integer type;
    private String emptyfile;
    private String resource;
}
