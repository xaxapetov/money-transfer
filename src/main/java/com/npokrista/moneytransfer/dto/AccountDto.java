package com.npokrista.moneytransfer.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Описание тела запроса счета
 */
@Data
public class AccountDto {

    /**
     * Идентификатор аккаунта
     */
    @Min(value = 1, message = "Id must be greater than 0")
    @NotNull
    private Long id;

    /**
     * Баланс аккаунта
     */
    @DecimalMin(value = "0.0", message = "Balance must be not less than 0")
    @NotNull
    private BigDecimal balance;
}
