package com.userfront.dao;

import com.userfront.domain.BynAccount;
import org.springframework.data.repository.CrudRepository;

public interface BynAccountDao extends CrudRepository<BynAccount, Long> {

    BynAccount findByAccountNumber (int accountNumber);
}
