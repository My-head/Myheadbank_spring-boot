package com.userfront.service;

import java.security.Principal;

import com.userfront.domain.UsdAccount;
import com.userfront.domain.BynAccount;

public interface AccountService {
	
    UsdAccount createUsdAccount();
    
    BynAccount createBynAccount();
    
    void deposit(String accountType, double amount, Principal principal);
    
    void withdraw(String accountType, double amount, Principal principal);
    
}
