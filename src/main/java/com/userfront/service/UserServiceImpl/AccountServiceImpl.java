package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.UsdAccountDao;
import com.userfront.dao.BynAccountDao;
import com.userfront.domain.UsdAccount;
import com.userfront.domain.UsdTransaction;
import com.userfront.domain.BynAccount;
import com.userfront.domain.BynTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static int nextAccountNumber = 11223145;

    @Autowired
    private UsdAccountDao usdAccountDao;

    @Autowired
    private BynAccountDao bynAccountDao;

    @Autowired
    private UserService userService;
    
    @Autowired
    private TransactionService transactionService;

    public UsdAccount createUsdAccount() {
        UsdAccount usdAccount = new UsdAccount();
        usdAccount.setAccountBalance(new BigDecimal(0.0));
        usdAccount.setAccountNumber(accountGen());

        usdAccountDao.save(usdAccount);

        return usdAccountDao.findByAccountNumber(usdAccount.getAccountNumber());
    }

    public BynAccount createBynAccount() {
        BynAccount bynAccount = new BynAccount();
        bynAccount.setAccountBalance(new BigDecimal(0.0));
        bynAccount.setAccountNumber(accountGen());

        bynAccountDao.save(bynAccount);

        return bynAccountDao.findByAccountNumber(bynAccount.getAccountNumber());
    }
    
    public void deposit(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("USD")) {
            UsdAccount usdAccount = user.getUsdAccount();
            usdAccount.setAccountBalance(usdAccount.getAccountBalance().add(new BigDecimal(amount)));
            usdAccountDao.save(usdAccount);

            Date date = new Date();

            UsdTransaction usdTransaction = new UsdTransaction(date, "Взятие кредита", "$", "Завршена", amount, usdAccount.getAccountBalance(), usdAccount);
            transactionService.saveUsdDepositTransaction(usdTransaction);
            
        } else if (accountType.equalsIgnoreCase("BYN")) {
            BynAccount bynAccount = user.getBynAccount();
            bynAccount.setAccountBalance(bynAccount.getAccountBalance().add(new BigDecimal(amount)));
            bynAccountDao.save(bynAccount);

            Date date = new Date();
            BynTransaction bynTransaction = new BynTransaction(date, "Взятие кредита", "Br", "Завршена", amount, bynAccount.getAccountBalance(), bynAccount);
            transactionService.saveBynDepositTransaction(bynTransaction);
        }
    }
    
    public void withdraw(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("USD")) {
            UsdAccount usdAccount = user.getUsdAccount();
            usdAccount.setAccountBalance(usdAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            usdAccountDao.save(usdAccount);

            Date date = new Date();

            UsdTransaction usdTransaction = new UsdTransaction(date, "Погашение кредита", "$", "Завршено", amount, usdAccount.getAccountBalance(), usdAccount);
            transactionService.saveUsdWithdrawTransaction(usdTransaction);
        } else if (accountType.equalsIgnoreCase("BYN")) {
            BynAccount bynAccount = user.getBynAccount();
            bynAccount.setAccountBalance(bynAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            bynAccountDao.save(bynAccount);

            Date date = new Date();
            BynTransaction bynTransaction = new BynTransaction(date, "Погашение кредита", "Br", "Завршено", amount, bynAccount.getAccountBalance(), bynAccount);
            transactionService.saveBynWithdrawTransaction(bynTransaction);
        }
    }
    
    private int accountGen() {
        return ++nextAccountNumber;
    }

	

}
