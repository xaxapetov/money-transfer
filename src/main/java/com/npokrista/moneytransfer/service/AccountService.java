package com.npokrista.moneytransfer.service;

import com.npokrista.moneytransfer.dto.AccountDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис по работу со счетами
 *
 * @author Ryadnov
 */
public interface AccountService<T> {

    /**
     * @param accountDto тело запроса счёта при создании
     * @return созданный счёт
     */
    AccountDto create(AccountDto accountDto);

    /**
     * @param id идентификатор счёта
     * @return параметры созданного счёта
     */
    AccountDto getById(Long id);

    /**
     * @return параметры созданного счёта
     */
    List<T> getAll();

    /**
     * @param id     идентификатор счёта
     * @param amount сумма средств
     */
    void addMoney(Long id, BigDecimal amount);

    /**
     * @param id     идентификатор счёта
     * @param amount сумма средств
     */
    void withdraw(Long id, BigDecimal amount);

    /**
     * @param from   идентификатор счёта отправителя
     * @param to     идентификатор получтеля
     * @param amount сумма средств
     */
    void transfer(Long from, Long to, BigDecimal amount);
}
