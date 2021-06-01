package com.cargo;
        //интеграционное тестирование
import com.cargo.controller.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")//к этой бд система будем подключаться при тестировании
@AutoConfigureMockMvc //спринг создает стр-ру классов, кот. подменяет слой Mvc
@RunWith(SpringRunner.class) //окружение, которое будет стартовать тесты
@SpringBootTest
public class LoginTest {

    @Autowired
    private MockMvc mockMvc; //фейковое окружение
    @Autowired
    private MainController controller;

    @Test
    public void testHome() throws Exception {          //через подмененный вэб-слой
        this.mockMvc.perform(get("/"))  //делаем гет-запрос к стартовой странице
                .andDo(print())        //печатаем ошибки и их логи в консоль
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome, Guest!")));
    }

    @Test
    public void showTariffsNoLogin() throws Exception {
        this.mockMvc.perform(get("/tariffs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Current basic tariffs")));
    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/user-transp/user"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test        //тест для проверки авторизации
    public void accessAdminDeniedTest() throws Exception {
        this.mockMvc.perform(get("/orders")) //запрашиваем страницу, кот требует авторизации
                .andDo(print())   //принт логов в консоль
                .andExpect(status().is3xxRedirection()) //ожидаем переадресацию на логин
                .andExpect(redirectedUrl("http://localhost/login"));//ожидаемый урл
    }

    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/transportations-list-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("user").password("user"))//вызываем форму логина spring security
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test    //проверяем отбиву неправильных данных пользователя
    public void badCredentials() throws Exception {       //вводим запрос руками
        this.mockMvc.perform(post("/login").param("user", "1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
