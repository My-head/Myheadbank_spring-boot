package com.userfront.dao;

import com.userfront.domain.UsdAccount;
import org.springframework.data.repository.CrudRepository;

public interface UsdAccountDao extends CrudRepository<UsdAccount,Long> {

    UsdAccount findByAccountNumber (int accountNumber);
}
