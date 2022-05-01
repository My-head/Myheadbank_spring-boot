package com.userfront.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.userfront.domain.UsdTransaction;

public interface UsdTransactionDao extends CrudRepository<UsdTransaction, Long> {

    List<UsdTransaction> findAll();
}
