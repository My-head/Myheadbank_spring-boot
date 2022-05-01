package com.userfront.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.userfront.domain.BynTransaction;

public interface BynTransactionDao extends CrudRepository<BynTransaction, Long> {

    List<BynTransaction> findAll();
}

