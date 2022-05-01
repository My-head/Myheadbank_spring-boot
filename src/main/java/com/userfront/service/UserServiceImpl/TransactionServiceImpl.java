package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.UsdAccountDao;
import com.userfront.dao.UsdTransactionDao;
import com.userfront.dao.RecipientDao;
import com.userfront.dao.BynAccountDao;
import com.userfront.dao.BynTransactionDao;
import com.userfront.domain.UsdAccount;
import com.userfront.domain.UsdTransaction;
import com.userfront.domain.Recipient;
import com.userfront.domain.BynAccount;
import com.userfront.domain.BynTransaction;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {
	BigDecimal usdcourse = BigDecimal.valueOf(2.65);
   BigDecimal byncourse = BigDecimal.valueOf(0.377);

	@Autowired
	private UserService userService;
	
	@Autowired
	private UsdTransactionDao usdTransactionDao;
	
	@Autowired
	private BynTransactionDao bynTransactionDao;
	
	@Autowired
	private UsdAccountDao usdAccountDao;
	
	@Autowired
	private BynAccountDao bynAccountDao;
	
	@Autowired
	private RecipientDao recipientDao;
	

	public List<UsdTransaction> findUsdTransactionList(String username){
        User user = userService.findByUsername(username);
        List<UsdTransaction> usdTransactionList = user.getUsdAccount().getUsdTransactionList();

        return usdTransactionList;
    }

    public List<BynTransaction> findBynTransactionList(String username) {
        User user = userService.findByUsername(username);
        List<BynTransaction> bynTransactionList = user.getBynAccount().getBynTransactionList();

        return bynTransactionList;
    }

    public void saveUsdDepositTransaction(UsdTransaction usdTransaction) {
        usdTransactionDao.save(usdTransaction);
    }

    public void saveBynDepositTransaction(BynTransaction bynTransaction) {
        bynTransactionDao.save(bynTransaction);
    }
    
    public void saveUsdWithdrawTransaction(UsdTransaction usdTransaction) {
        usdTransactionDao.save(usdTransaction);
    }

    public void saveBynWithdrawTransaction(BynTransaction bynTransaction) {
        bynTransactionDao.save(bynTransaction);
    }
    
    public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, UsdAccount usdAccount, BynAccount bynAccount) throws Exception {
        if (transferFrom.equalsIgnoreCase("USD") && transferTo.equalsIgnoreCase("BYN")) {
            usdAccount.setAccountBalance(usdAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            bynAccount.setAccountBalance(bynAccount.getAccountBalance().add(new BigDecimal(amount)));
            usdAccountDao.save(usdAccount);
            bynAccountDao.save(bynAccount);

            Date date = new Date();

            UsdTransaction usdTransaction = new UsdTransaction(date, "Межвалютный перевод долга от"+transferFrom+" к "+transferTo, "$", "Завершена", Double.parseDouble(amount), usdAccount.getAccountBalance(), usdAccount);
            usdTransactionDao.save(usdTransaction);
        } else if (transferFrom.equalsIgnoreCase("BYN") && transferTo.equalsIgnoreCase("USD")) {
            usdAccount.setAccountBalance(usdAccount.getAccountBalance().add(new BigDecimal(amount)));
            bynAccount.setAccountBalance(bynAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            usdAccountDao.save(usdAccount);
            bynAccountDao.save(bynAccount);

            Date date = new Date();

            BynTransaction bynTransaction = new BynTransaction(date, "Межвалютный перевод долга от "+transferFrom+" к "+transferTo, "Br", "Завершена", Double.parseDouble(amount), bynAccount.getAccountBalance(), bynAccount);
            bynTransactionDao.save(bynTransaction);
        } else {
            throw new Exception("Ошибка транзакции");
        }
    }
    
    public List<Recipient> findRecipientList(Principal principal) {
        String username = principal.getName();
        List<Recipient> recipientList = recipientDao.findAll().stream() 			
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))	
                .collect(Collectors.toList());

        return recipientList;
    }

    public Recipient saveRecipient(Recipient recipient) {
        return recipientDao.save(recipient);
    }

    public Recipient findRecipientByName(String recipientName) {
        return recipientDao.findByName(recipientName);
    }

    public void deleteRecipientByName(String recipientName) {
        recipientDao.deleteByName(recipientName);
    }
    
    public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, UsdAccount usdAccount, BynAccount bynAccount) {
        if (accountType.equalsIgnoreCase("USD")) {
            usdAccount.setAccountBalance(usdAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            usdAccountDao.save(usdAccount);

            Date date = new Date();

            UsdTransaction usdTransaction = new UsdTransaction(date, "Погашение долга  "+recipient.getName(), "Валюта", "Завершена", Double.parseDouble(amount), usdAccount.getAccountBalance(), usdAccount);
            usdTransactionDao.save(usdTransaction);
        } else if (accountType.equalsIgnoreCase("BYN")) {
            bynAccount.setAccountBalance(bynAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            bynAccountDao.save(bynAccount);

            Date date = new Date();

            BynTransaction bynTransaction = new BynTransaction(date, "Погашение долга "+recipient.getName(), "Валюта", "Завершена", Double.parseDouble(amount), bynAccount.getAccountBalance(), bynAccount);
            bynTransactionDao.save(bynTransaction);
        }
    }
}
