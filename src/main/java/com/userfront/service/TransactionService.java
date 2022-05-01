package com.userfront.service;

import java.security.Principal;
import java.util.List;

import com.userfront.domain.UsdAccount;
import com.userfront.domain.UsdTransaction;
import com.userfront.domain.Recipient;
import com.userfront.domain.BynAccount;
import com.userfront.domain.BynTransaction;

public interface TransactionService {
    
	List<UsdTransaction> findUsdTransactionList(String username);

    List<BynTransaction> findBynTransactionList(String username);

    void saveUsdDepositTransaction(UsdTransaction usdTransaction);

    void saveBynDepositTransaction(BynTransaction bynTransaction);
    
    void saveUsdWithdrawTransaction(UsdTransaction usdTransaction);
    
    void saveBynWithdrawTransaction(BynTransaction bynTransaction);
    
    void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, UsdAccount usdAccount, BynAccount bynAccount) throws Exception;
    
    List<Recipient> findRecipientList(Principal principal);

    Recipient saveRecipient(Recipient recipient);

    Recipient findRecipientByName(String recipientName);

    void deleteRecipientByName(String recipientName);
    
    void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, UsdAccount usdAccount, BynAccount bynAccount);
}
