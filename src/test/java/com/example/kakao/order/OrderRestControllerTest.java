package com.example.kakao.order;

import com.example.kakao.MyRestDoc;
import com.example.kakao._core.errors.exception.Exception400;
import com.example.kakao._core.errors.exception.Exception404;
import com.example.kakao.cart.CartJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderRestControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @MockBean
    private CartJPARepository cartJPARepository;

    @WithUserDetails(value = "ssarmango@nate.com")
    @Test
    public void save_test() throws Exception {

        ResultActions resultActions = mvc.perform(
                post("/carts/orders/save")
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Save Test: " + responseBody);

        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @WithUserDetails(value = "ssarmango@nate.com")
    @Test
    public void findById_test() throws Exception {
        int id = 1;
        ResultActions resultActions = mvc.perform(
                get("/carts/orders/" + id)
        );

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("FindById Test: " + responseBody);

        resultActions.andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.id").value(1))
                .andExpect(jsonPath("$.response.products[0].productName").value(
                        "기본에 슬라이딩 지퍼백 크리스마스/플라워에디션 에디션 외 주방용품 특가전"))
                .andExpect(jsonPath("$.response.products[0].items[0].id").value(1))
                .andExpect(jsonPath("$.response.products[0].items[0].quantity").value(5))
                .andExpect(jsonPath("$.response.products[0].items[0].price").value(50000))
                .andExpect(jsonPath("$.response.products[0].items[0].optionName").value(
                        "01. 슬라이딩 지퍼백 크리스마스에디션 4종"));

        resultActions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @WithUserDetails(value = "ssarmango@nate.com")
    @Test
    public void findById_test_with_invalid_id() throws Exception {
        int id = -1; // invalid id
        ResultActions resultActions = mvc.perform(
                get("/carts/orders/" + id)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("findById_test_with_invalid_id : " + responseBody);

        resultActions.andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception404))
                .andExpect(result -> assertEquals("해당 주문이 존재하지 않습니다 : " + id,
                        result.getResolvedException().getMessage()));
    }

    @WithUserDetails(value = "ssarmango@nate.com")
    @Test
    public void save_test_with_empty_cart() throws Exception {
        Mockito.when(cartJPARepository.findByUserIdOrderByOptionIdAsc(Mockito.anyInt()))
                .thenReturn(new ArrayList<>());

        ResultActions resultActions = mvc.perform(
                post("/carts/orders/save")
        );

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("save_test_with_empty_cart : " + responseBody);

        resultActions.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception400))
                .andExpect(
                        result -> assertEquals("장바구니가 비어있습니다.", result.getResolvedException().getMessage()));
    }


}