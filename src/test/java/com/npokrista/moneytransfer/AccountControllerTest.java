package com.npokrista.moneytransfer;


import com.npokrista.moneytransfer.dto.AccountDto;
import com.npokrista.moneytransfer.rest.AccountController;
import com.npokrista.moneytransfer.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void createShouldReturn200() throws Exception{
        AccountDto acc = new AccountDto();
        acc.setId(4L);
        acc.setBalance(new BigDecimal("1700.0"));
        when(accountService.getById(anyLong())).thenReturn(acc);
        this.mockMvc.perform(get("/api/v3/accounts/4")).andExpect(status().isOk());
                //.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }
    @Test
    public void createShouldReturn400() throws Exception{
       // when(accountService.getById(anyLong())).thenReturn(new AccountDto(){{setId(4L); setBalance(new BigDecimal("1700.0"));}});
       // this.mockMvc.perform(get("/api/v3/accounts/5")).andExpect(status().is4xxClientError());

    }
}
