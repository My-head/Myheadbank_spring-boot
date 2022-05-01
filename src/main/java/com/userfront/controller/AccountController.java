package com.userfront.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.UsdAccount;
import com.userfront.domain.UsdTransaction;
import com.userfront.domain.BynAccount;
import com.userfront.domain.BynTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping("/usdAccount")
	public String usdAccount(Model model, Principal principal) {
		List<UsdTransaction> usdTransactionList = transactionService.findUsdTransactionList(principal.getName());
		
		User user = userService.findByUsername(principal.getName());
        UsdAccount usdAccount = user.getUsdAccount();

        model.addAttribute("usdAccount", usdAccount);
        model.addAttribute("usdTransactionList", usdTransactionList);
		
		return "usdAccount";
	}

	@RequestMapping("/bynAccount")
    public String bynAccount(Model model, Principal principal) {
		List<BynTransaction> bynTransactionList = transactionService.findBynTransactionList(principal.getName());
        User user = userService.findByUsername(principal.getName());
        BynAccount bynAccount = user.getBynAccount();

        model.addAttribute("bynAccount", bynAccount);
        model.addAttribute("bynTransactionList", bynTransactionList);

        return "bynAccount";
    }
	
	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
    public String deposit(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");

        return "deposit";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String depositPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        accountService.deposit(accountType, Double.parseDouble(amount), principal);

        return "redirect:/userFront";
    }
    
    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public String withdraw(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");

        return "withdraw";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdrawPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        accountService.withdraw(accountType, Double.parseDouble(amount), principal);

        return "redirect:/userFront";
    }
}
