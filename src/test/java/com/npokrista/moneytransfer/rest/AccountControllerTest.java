package com.npokrista.moneytransfer.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.npokrista.moneytransfer.dto.AccountDto;
import com.npokrista.moneytransfer.service.AccountService;
import com.npokrista.moneytransfer.service.exception.IncorrectValueException;
import com.npokrista.moneytransfer.service.exception.NoEntityException;
import com.npokrista.moneytransfer.service.exception.ObjectIsExist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;

    private final AccountDto testDtoId0 = new AccountDto(){{setId(0L); setBalance(new BigDecimal("1000.0"));}};
    private final AccountDto testDto = new AccountDto(){{setId(1L); setBalance(new BigDecimal("1000.0"));}};
    private final AccountDto testDtoNull = null;
    private final AccountDto testDtoNegativeBalance = new AccountDto(){{setId(2L); setBalance(new BigDecimal("-1000.0"));}};
    List<AccountDto> accountDtoList;

    @PostConstruct
    public void incorrectData(){
        accountDtoList = new ArrayList<>();
        accountDtoList.add(testDtoId0);
        accountDtoList.add(testDtoNull);
        accountDtoList.add(testDtoNegativeBalance);
    }
    public String testDtoJSON(Object DTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(DTO);
        return  requestJson;
    }


    @Test
    public void createShouldReturn200() throws Exception{

        when(accountService.create(testDto)).thenReturn(testDto);
        System.out.println(accountService.create(testDto));
        this.mockMvc.perform(post("/api/v3/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testDtoJSON(testDto))).andExpect(status().isOk());
    }

    @Test
    public void createShouldReturn400_ID_IS_EXIST() throws Exception{

        when(accountService.create(any())).thenThrow(new ObjectIsExist("test in createShouldReturn400_ID_IS_EXIST"));
        this.mockMvc.perform(post("/api/v3/accounts")).andExpect(status().is4xxClientError());
    }

    @Test
    public void createShouldReturn400_INCORRECT_DATA() throws Exception{
       for(AccountDto acc: accountDtoList){
           when(accountService.create(acc)).thenReturn(acc);
           this.mockMvc.perform(post("/api/v3/accounts")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(testDtoJSON(acc))).andExpect(status().is4xxClientError());
       }

    }


    @Test
    public void getAccountShouldReturn200() throws Exception{
        when(accountService.getById(testDto.getId())).thenReturn(testDto);
        this.mockMvc.perform(get("/api/v3/accounts/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testDtoJSON(testDto)))
                .andExpect(status().isOk());

    }
    @Test
    public void getAccountShouldReturn400_NO_ENTITY() throws Exception{
        when(accountService.getById(anyLong())).thenThrow(new NoEntityException("Test"));
        this.mockMvc.perform(get("/api/v3/accounts/1")).andExpect(status().is4xxClientError());
    }

    @Test
    public void getAccountShouldReturn400_INCORRECT_DATA() throws Exception {
        when(accountService.getById(testDtoId0.getId())).thenReturn(testDtoId0);
        this.mockMvc.perform(get("/api/v3/accounts/-3")).andExpect(status().is4xxClientError());
    }

    @Test
    public void getAccountsShouldReturn200() throws Exception{
        List<AccountDto> acc = new ArrayList<>();
        acc.add(testDto);
        when(accountService.getAll()).thenReturn(acc);
        this.mockMvc.perform(get("/api/v3/accounts/all"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testDtoJSON(acc)))
                .andExpect(status().isOk());
    }

    @Test
    public void addMoneyShouldReturn200() throws Exception{
        doNothing().when(accountService).addMoney(anyLong(), any());;
        this.mockMvc.perform(put("/api/v3/accounts/1/add/1000"))
                .andExpect(status().isOk());
    }

    @Test
    public void addMoneyMoneyShouldReturn400_INCORRECT_DATA() throws Exception{

        doThrow(new NoEntityException("test in addMoneyShouldReturn400_INCORRECT_DATA"))
                .when(accountService).addMoney(anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/1/add/1000"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void addMoneyShouldReturn400_NAGATIVE_AMOUNT() throws Exception{

        doNothing().when(accountService).addMoney(anyLong(), any());;
        this.mockMvc.perform(put("/api/v3/accounts/1/add/-1000"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void addMoneyShouldReturn400_ID_IS_0_OR_LESS() throws Exception{

        doNothing().when(accountService).addMoney(anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/-1/add/1000"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void withdrawMoneyShouldReturn200() throws Exception{
        doNothing().when(accountService).withdraw(anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/1/withdraw/1000"))
                .andExpect(status().isOk());
    }

    @Test
    public void withdrawMoneyShouldReturn400_INCORRECT_DATA() throws Exception{

        doThrow(new NoEntityException("test in withdrawMoneyShouldReturn400_INCORRECT_DATA"))
            .when(accountService).withdraw(anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/1/withdraw/1000"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void withdrawMoneyShouldReturn400_NAGATIVE_AMOUNT() throws Exception{

        doNothing().when(accountService).withdraw(anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/1/withdraw/-1000"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void withdrawMoneyShouldReturn400_ID_IS_0_OR_LESS() throws Exception{

        doNothing().when(accountService).withdraw(anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/-1/withdraw/1000"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void transferMoneyShouldReturn200() throws Exception{
        doNothing().when(accountService).transfer(anyLong(),anyLong(), any());
        this.mockMvc.perform(patch("/api/v3/accounts/1/transfer/2/1000"))
                .andExpect(status().isOk());
    }

    @Test
    public void transferMoneyShouldReturn400_INCORRECT_DATA() throws Exception{

        doThrow(new NoEntityException("test in withdrawMoneyShouldReturn400_INCORRECT_DATA"))
                .when(accountService).transfer(anyLong(),anyLong(), any());
        this.mockMvc.perform(patch("/api/v3/accounts/1/transfer/2/1000"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void transferMoneyShouldReturn400_NAGATIVE_AMOUNT() throws Exception{

        doNothing().when(accountService).transfer(anyLong(),anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/1/transfer/2/-1000"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void transferMoneyShouldReturn400_ID_IS_0_OR_LESS() throws Exception{

        doNothing().when(accountService).transfer(anyLong(),anyLong(), any());
        this.mockMvc.perform(put("/api/v3/accounts/-1/transfer/-2/1000"))
                .andExpect(status().is4xxClientError());
    }
}
