package com.cargo.service;
    //unit - тестирование, модульное тестирование
import com.cargo.model.user.Role;
import com.cargo.model.user.User;
import com.cargo.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean         //создаем подменный объект бд
    private UserRepo userRepo;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldCreateUser(){
        User user = new User();
        boolean isUserCreated = userService.addUser(user);

        Assert.assertTrue(isUserCreated);                    //проверяем, что установлена роль USER
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
//проверяем сколько раз был вызван userRepo и было ли сохранение
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void createUserFail(){
        User user = new User();
        user.setUsername("noname");
//эмулируем ответ БД, что такой юзер уже существует
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("noname");

        boolean isCreated = userService.addUser(user);

        Assert.assertFalse(isCreated);
    //дополнительно проверяем, что новый пользователь не создан
        Mockito.verify(userRepo, Mockito.times(0)).save(user);
    }
}
