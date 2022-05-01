package com.userfront.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UsdAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int accountNumber;
    private BigDecimal accountBalance;

    @OneToMany(mappedBy = "usdAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UsdTransaction> usdTransactionList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public List<UsdTransaction> getUsdTransactionList() {
        return usdTransactionList;
    }

    public void setUsdTransactionList(List<UsdTransaction> usdTransactionList) {
        this.usdTransactionList = usdTransactionList;
    }


}



