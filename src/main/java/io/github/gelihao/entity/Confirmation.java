package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Confirmation {

    @TableId
    private Integer id;
    private String identifier;
    private String assetliabilityratio;
    private String eps;
    private String roe;
    private String operatingmargin;
    private String currentratio;
    private String contractratio;
    private String accountsratio;
    private String ifaccoutsoverdue;
    private String ifticketnotaccept;
    private String ifpenalty;
    private String ifinsurance;
    private String ifdispute;
    private String ifloan;
    private String taxcredit;
    private String charitablecontributions;
    private String employcontributions;
    private String filepath;
    private Date editdate;
}
