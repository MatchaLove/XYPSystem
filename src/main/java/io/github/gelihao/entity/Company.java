package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @TableId
    private Integer id;
    private String identifier;
    private String companyname;
    private String chargeman;
    private String address;
    private String phone;
    private String email;
    private Float credit;
    private Date createdtime;
    private Date updatedtime;

}
