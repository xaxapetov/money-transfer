package com.npokrista.moneytransfer.service.impl;

import com.npokrista.moneytransfer.dto.AccountDto;
import com.npokrista.moneytransfer.model.Account;
import com.npokrista.moneytransfer.repository.AccountRepository;
import com.npokrista.moneytransfer.service.AccountService;
import com.npokrista.moneytransfer.service.exception.IncorrectValueException;
import com.npokrista.moneytransfer.service.exception.ObjectIsExist;
import com.npokrista.moneytransfer.service.exception.NoEntityException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private ModelMapper modelMapper;

    @PostConstruct
    private void init() {
        modelMapper = new ModelMapper();
    }


    @Override
    public AccountDto create(AccountDto accountDto) throws ObjectIsExist {
        if (accountRepository.existsById(accountDto.getId())) {
            throw new ObjectIsExist("Object is exist. Id = " + accountDto.getId());
        }
        return modelMapper.map(accountRepository.save(modelMapper.map(accountDto, Account.class)), AccountDto.class);
    }

    @Override
    public AccountDto getById(Long id) throws NoEntityException {
        return modelMapper.map(accountRepository.findById(id)
                .orElseThrow(() -> new NoEntityException("Exception in getById method")), AccountDto.class);
    }

    @Override
    public List<AccountDto> getAll() {
        return accountRepository
                .findAll()
                .stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void addMoney(Long id, BigDecimal amount) {
        if (!accountRepository.existsById(id)) {
            throw new NoEntityException("Exception in addMoney method");
        }
        accountRepository.addMoney(id, amount);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void withdraw(Long id, BigDecimal amount) throws IncorrectValueException {
        AccountDto EntityInBase = modelMapper.map(accountRepository.findById(id)
                .orElseThrow(() -> new NoEntityException("Exception in withdraw method")), AccountDto.class);

        if (amount.compareTo(EntityInBase.getBalance()) > 0) {
            throw new IncorrectValueException("Exception in withdraw method");
        } else {
            accountRepository.withdraw(id, amount);
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(Long from, Long to, BigDecimal amount) throws IncorrectValueException, NoEntityException {
        withdraw(from, amount);
        addMoney(to, amount);
    }
}
