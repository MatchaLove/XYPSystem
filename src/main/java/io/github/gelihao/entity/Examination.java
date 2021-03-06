package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Examination {

    @TableId
    private Integer id;
    private String identifier;
    private String companyname;
    private String businessid;
    private String businessname;
    private String examinator;
    private Integer status;//0未审核 1yes 2不通过
    private String remark;
    private Date editdate;
}
