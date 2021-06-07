package com.cargo;

import com.cargo.controller.MainController;
import com.cargo.controller.TranspController;
import com.cargo.service.TariffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
@WithUserDetails("user")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class TranspControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainController mainController;
    @Autowired
    private TranspController transpController;
    @Autowired
    private TariffService tariffService;

    @Test
    public void userPageTest() throws Exception {
        this.mockMvc.perform(get("/user-transp/user"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(xpath("/html/body/div[1]/div/nav/div/div[2]").string("user"));
    }

    @Test
    public void showUserTransportationsPageTest() throws Exception {
        this.mockMvc.perform(get("/user-transp/user"))
                .andDo(print())  //проверяет, что пользователь был корректно аутентифицирован
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(xpath("//div[@id='user-transportations-list']/table/tbody/tr").nodeCount(3));
    }

//    @Test
//    public void addTransportationTest() throws Exception{
//        this.mockMvc.perform(post("/main").param("comment", "Some new shipping").with(csrf()))
//                .andDo(print())
//                .andExpect(SecurityMockMvcResultMatchers.authenticated())
//                .andExpect(xpath("//div[@id='transportations-list']/table/tbody/tr").nodeCount(5))
//                .andExpect(xpath("//div[@id='transportations-list']/table/tbody/tr").exists())
//                .andExpect(xpath("//div[@id='transportations-list']/table/tbody/tr/td").string("Some new"));
//    }
//     @Test
//     public void showNewTransportationForm() throws Exception {
//         this.mockMvc.perform(get("/showNewTransportationForm"))
//                 .andDo(print())
//                 .andExpect(SecurityMockMvcResultMatchers.authenticated())
//                 .andExpect(status().isOk());
//     }
}
