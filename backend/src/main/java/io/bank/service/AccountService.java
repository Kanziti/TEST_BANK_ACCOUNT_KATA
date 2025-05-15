package io.bank.service;

import io.bank.domain.Account;
import io.bank.domain.Transaction;
import io.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount() {
        Account account = new Account();
        return accountRepository.save(account);
    }

    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {
        Account account = getAccountById(accountId);
        account.deposit(amount);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {
        Account account = getAccountById(accountId);
        account.withdraw(amount);
        accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    }

    public BigDecimal getBalance(Long accountId) {
        Account account = getAccountById(accountId);
        return account.getBalance();
    }

    public List<Transaction> getTransactions(Long accountId) {
        Account account = getAccountById(accountId);
        return account.getTransactions();
    }
}
