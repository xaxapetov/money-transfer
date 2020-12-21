package com.npokrista.moneytransfer.rest;

import com.npokrista.moneytransfer.dto.AccountDto;
import com.npokrista.moneytransfer.service.AccountService;
import com.npokrista.moneytransfer.service.exception.IncorrectValueException;
import com.npokrista.moneytransfer.service.exception.ObjectIsExist;
import com.npokrista.moneytransfer.service.exception.NoEntityException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * Контроллер для работы со счетами
 *
 * @author Ryadnov Nikita
 */
@RestController
@RequestMapping("/api/v3/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @CrossOrigin
    @PostMapping(produces = "application/json; charset=utf-8")
    @ApiOperation(value = "Создание счёта", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Запрос выполнен успешно"),
            @ApiResponse(code = 400, message = "Неправильный запрос"),
            @ApiResponse(code = 500, message = "Ошибка во время выполнения запроса")
    })
    public ResponseEntity<AccountDto> create(@NotNull @Valid @RequestBody AccountDto accountDto) throws ObjectIsExist {

        AccountDto newAccount = accountService.create(accountDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("")
                .buildAndExpand(newAccount)
                .toUri();
        System.out.println(location);
        return ResponseEntity
                .created(location)
                .body(newAccount);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @ApiOperation(value = "Возвращение счета по идентификатору", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Запрос выполнен успешно"),
            @ApiResponse(code = 400, message = "Неправильный запрос"),
            @ApiResponse(code = 500, message = "Ошибка во время выполнения запроса")
    })
    public ResponseEntity<AccountDto> getAccount(@Min(1) @PathVariable("id") Long id) throws NoEntityException {

        return ResponseEntity.ok(accountService.getById(id));
    }

    @CrossOrigin
    @GetMapping("/all")
    @ApiOperation(value = "Возвращение всех счетов", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Запрос выполнен успешно"),
            @ApiResponse(code = 400, message = "Неправильный запрос"),
            @ApiResponse(code = 500, message = "Ошибка во время выполнения запроса")
    })
    public ResponseEntity<List<AccountDto>> getAccounts() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @CrossOrigin
    @PutMapping("/{id}/add/{amount}")
    @ApiOperation(value = "Пополнение счёта", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Запрос выполнен успешно"),
            @ApiResponse(code = 400, message = "Неправильный запрос"),
            @ApiResponse(code = 500, message = "Ошибка во время выполнения запроса")
    })
    public ResponseEntity<?> addMoney(@PathVariable("id") @Min(1) Long id,
                                      @PathVariable("amount") @DecimalMin(value = "0.0") BigDecimal amount) {
        accountService.addMoney(id, amount);
        return ResponseEntity.ok().build();

    }

    @CrossOrigin
    @PutMapping("/{id}/withdraw/{amount}")
    @ApiOperation(value = "Снятие средств со счёта", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Запрос выполнен успешно"),
            @ApiResponse(code = 400, message = "Неправильный запрос"),
            @ApiResponse(code = 500, message = "Ошибка во время выполнения запроса")
    })
    public ResponseEntity<?> withdrawMoney(@Min(1) @PathVariable("id") Long id,
                                           @DecimalMin(value = "0.0") @PathVariable("amount") BigDecimal amount) throws IncorrectValueException {
        accountService.withdraw(id, amount);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @PatchMapping("/{from}/transfer/{to}/{amount}")
    @ApiOperation(value = "Перевод средств между счетами", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Запрос выполнен успешно"),
            @ApiResponse(code = 400, message = "Неправильный запрос"),
            @ApiResponse(code = 500, message = "Ошибка во время выполнения запроса")
    })
    public ResponseEntity<?> transferMoney(@Min(1) @PathVariable("from") Long from,
                                           @Min(1) @PathVariable("to") Long to,
                                           @DecimalMin(value = "0.0") @PathVariable("amount") BigDecimal amount) throws IncorrectValueException {
        accountService.transfer(from, to, amount);
        return ResponseEntity.ok().build();
    }
}
