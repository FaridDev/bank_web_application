package com.bank.mvc.domain.service.spring;

import com.bank.mvc.dao.AccountDao;
import com.bank.mvc.domain.service.AccountService;
import com.bank.mvc.domain.service.UserService;
import com.bank.mvc.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Zalman on 10.05.2015.
 */

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserService userService;

    @Override
    public Account getAccountById(long accountId) {
        return accountDao.getById(accountId);
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accountDao.getAll();
    }

    @Override
    public void saveAccount(Account account) {
        if (account.getUser() == null) {
            account.setUser(userService.getUserById(account.getUserId()));
        }
        accountDao.save(account);
    }

}
