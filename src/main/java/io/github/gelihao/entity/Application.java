package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @TableId
    private Integer id;
    private Integer ifmain;
    private Integer ifsecond;
    private String identifier;
    private String businessid;
    private String filepath;
    private String secondfilepath;
    private Date editdate;
}
