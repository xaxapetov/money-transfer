package com.npokrista.moneytransfer.repository;

import com.npokrista.moneytransfer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    @Modifying
    @Query("UPDATE Account u SET u.balance = u.balance + :amount WHERE u.id = :id")
    int addMoney(@Param("id") Long id, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("UPDATE Account u SET u.balance = u.balance - :amount WHERE u.id = :id")
    int withdraw(@Param("id") Long id, @Param("amount") BigDecimal amount);
}