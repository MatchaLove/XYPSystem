package io.github.gelihao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String qualification;
    private Integer ifqualification;
    private String role;
    private String company;
}
