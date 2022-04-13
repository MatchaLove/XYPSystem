package io.github.gelihao;

import io.github.gelihao.dao.UserDao;
import io.github.gelihao.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserDaoTest
{
    @Resource
    private UserDao userDao;

    @Test
    public void selectListTest(){
        List<User> users = userDao.selectList(null);
        users.forEach(System.out::println);
    }


}
