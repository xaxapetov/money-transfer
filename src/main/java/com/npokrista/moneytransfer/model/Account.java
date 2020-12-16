package com.npokrista.moneytransfer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Описание сущности счёта
 *
 * @author Ryadnov Nikita
 */
@Table(name = "accounts", schema = "public")
@Entity
@Getter
@Setter
public class Account {

    /**
     * Идентификатор аккаунта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Баланс аккаунта
     */
    @Column(name = "balance")
    private BigDecimal balance;
}
