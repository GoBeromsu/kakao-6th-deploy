package com.example.kakao.user;

import com.example.kakao.MyRestDoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRestControllerTest extends MyRestDoc {

  @Autowired
  private ObjectMapper om;

  @Test
  public void join_test() throws Exception {
    UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
    joinDTO.setUsername("ssarmango");
    joinDTO.setEmail("gobeumsu@gmail.com");
    joinDTO.setPassword("meta123!");
    String requestBody = om.writeValueAsString(joinDTO);
    ResultActions resultActions = mvc.perform(
        post("/join")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("Join Test: " + responseBody);
    resultActions.andExpect(jsonPath("$.success").value("true"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }

  @Test
  public void join_test_same_email() throws Exception {
    UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
    joinDTO.setUsername("ssarmango");
    joinDTO.setEmail("gobeumsu@gmail.com");
    joinDTO.setPassword("meta123!");
    String requestBody = om.writeValueAsString(joinDTO);
    ResultActions resultActions = mvc.perform(
        post("/join")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("Join Test Same Email: " + responseBody);
    resultActions.andExpect(jsonPath("$.success").value("true"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }

  @Test
  public void join_test_wrong_password() throws Exception {
    UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
    joinDTO.setUsername("ssarmango");
    joinDTO.setEmail("gobeumsu@gmail.com");
    joinDTO.setPassword("meta1!");
    String requestBody = om.writeValueAsString(joinDTO);
    ResultActions resultActions = mvc.perform(
        post("/join")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("Join Test Wrong Password: " + responseBody);
    resultActions.andExpect(jsonPath("$.success").value("false"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }

  @Test
  public void join_test_wrong_email() throws Exception {
    UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
    joinDTO.setUsername("ssarmango");
    joinDTO.setEmail("gobeumsugmail.com");
    joinDTO.setPassword("meta123!");
    String requestBody = om.writeValueAsString(joinDTO);
    ResultActions resultActions = mvc.perform(
        post("/join")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("Join Test Wrong Email: " + responseBody);
    resultActions.andExpect(jsonPath("$.success").value("false"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }

  @Test
  public void join_test_wrong_username() throws Exception {
    UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
    joinDTO.setUsername("ssarman");
    joinDTO.setEmail("gobeumsu@gmail.com");
    joinDTO.setPassword("meta123!");
    String requestBody = om.writeValueAsString(joinDTO);
    ResultActions resultActions = mvc.perform(
        post("/join")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("Join Test Wrong Username: " + responseBody);
    resultActions.andExpect(jsonPath("$.success").value("false"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }

  @Test
  public void login_test() throws Exception {
    UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
    loginDTO.setEmail("ssarmango@nate.com");
    loginDTO.setPassword("meta1234!");
    String requestBody = om.writeValueAsString(loginDTO);
    ResultActions resultActions = mvc.perform(
        post("/login")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("Login Test: " + responseBody);
    resultActions.andExpect(jsonPath("$.success").value("true"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }


  @Test
  public void login_test_invalid_email() throws Exception {
    UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
    loginDTO.setEmail("invalidemail");
    loginDTO.setPassword("ValidPassword123!");
    String requestBody = om.writeValueAsString(loginDTO);
    ResultActions resultActions = mvc.perform(
        post("/login")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    resultActions.andExpect(jsonPath("$.success").value("false"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }

  @Test
  public void login_test_invalid_password() throws Exception {
    UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
    loginDTO.setEmail("validemail@example.com");
    loginDTO.setPassword("invalidpassword");
    String requestBody = om.writeValueAsString(loginDTO);
    ResultActions resultActions = mvc.perform(
        post("/login")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    resultActions.andExpect(jsonPath("$.success").value("false"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }

  @Test
  public void login_test_invalid_email_password() throws Exception {
    UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
    loginDTO.setEmail("invalidemail");
    loginDTO.setPassword("invalidpassword");
    String requestBody = om.writeValueAsString(loginDTO);
    ResultActions resultActions = mvc.perform(
        post("/login")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
    );
    resultActions.andExpect(jsonPath("$.success").value("false"));
    resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
  }
}
