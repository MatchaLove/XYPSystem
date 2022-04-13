package io.github.gelihao;

import io.github.gelihao.entity.User;
import io.github.gelihao.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void getByIdTest(){
        User user = userService.getById(1);
        System.out.println(user);
    }

}
