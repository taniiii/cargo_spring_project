package com.cargo;

import com.cargo.controller.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(value = {"/create-user-before.sql", "/table_tariff_before.sql", "/transportations-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/transportations-list-after.sql", "/table_tariff_after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource("/application-test.properties")
@WithUserDetails("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainController mainController;

    @Test
    public void allTransportationsAdminPageTest() throws Exception {
        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(xpath("/html/body/div[1]/div/nav/div/div[2]").string("test"));
    }

    @Test
    public void transportationsListTest() throws Exception{
        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(xpath("//div[@id='transportations-list']/table/tbody/tr").nodeCount(4));
    }

//        @Test
//    public void filterCommentTest() throws Exception {
//        this.mockMvc.perform(get("/orders").param("filter", "France"))
//                .andDo(print())
//                .andExpect(SecurityMockMvcResultMatchers.authenticated())//польз-ль должен быть авторизов.
//                .andExpect(xpath("//div[@id='transportations-list']/table/tbody/tr").nodeCount(2))
//                .andExpect(xpath("//div[@id='transportations-list']/table/tbody/tr").exists());
//    }
}
