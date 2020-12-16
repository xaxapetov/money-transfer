package com.npokrista.moneytransfer.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.npokrista.moneytransfer.dto.AccountDto;
import com.npokrista.moneytransfer.service.AccountService;
import com.npokrista.moneytransfer.service.exception.IncorrectValueException;
import com.npokrista.moneytransfer.service.exception.NoEntityException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;

    AccountDto testDto1;
    AccountDto testDto2;
    AccountDto testDto3;

    @Before
    public void testDto(){
        testDto1 = new AccountDto(){{setId(0L); setBalance(new BigDecimal("1700.0"));}};
        testDto2 = new AccountDto(){{setId(1L); setBalance(new BigDecimal("1700.0"));}};
        testDto3 = new AccountDto(){{setId(2L); setBalance(new BigDecimal("-1700.0"));}};
    }
    public String testDtoJSON(AccountDto DTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(DTO);
        return  requestJson;
    }
    @Test
    public void createShouldReturn200() throws Exception{

        when(accountService.create(testDto2)).thenReturn(testDto2);
        this.mockMvc.perform(post("/api/v3/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testDtoJSON(testDto2))).andExpect(status().isOk());

    }

    @Test
    public void createShouldReturn400_INCORRECT_VALUE() throws Exception{
        when(accountService.create(any())).thenThrow(new IncorrectValueException("test in createShouldReturn400_INCORRECT_VALUE"));
        this.mockMvc.perform(post("/api/v3/accounts")).andExpect(status().is4xxClientError());
    }

    @Test
    public void getAccountShouldReturn200() throws Exception{
        when(accountService.getById(testDto2.getId())).thenReturn(testDto2);
        System.out.println(accountService.getById(4L));
        this.mockMvc.perform(get("/api/v3/accounts/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    public void getAccountShouldReturn400_NO_ENTITY() throws Exception{
        when(accountService.getById(anyLong())).thenThrow(new NoEntityException("Test in getAccountShouldReturn400_NO_ENTITY"));
        this.mockMvc.perform(get("/api/v3/accounts/4")).andExpect(status().is4xxClientError());
    }


}
