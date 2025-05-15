package io.bank.service;

import io.bank.domain.Account;
import io.bank.domain.Transaction;
import io.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private final Long accountId = 1L;
    private final BigDecimal initialBalance = BigDecimal.valueOf(100.00);

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setBalance(initialBalance);
    }

    @Test
    void createAccount_should_create_and_save_new_account() {
        Account newAccount = new Account();
        when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenReturn(newAccount);

        Account result = accountService.createAccount();

        assertNotNull(result);
        verify(accountRepository, times(1)).save(ArgumentMatchers.any(Account.class));
    }

    @Test
    void deposit_should_increase_balance_and_save_account() {
        BigDecimal depositAmount = BigDecimal.valueOf(50.00);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenReturn(testAccount);

        accountService.deposit(accountId, depositAmount);

        assertEquals(initialBalance.add(depositAmount).setScale(2, RoundingMode.HALF_EVEN), testAccount.getBalance());
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void withdraw_should_decrease_balance_and_save_account() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(50.00);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenReturn(testAccount);

        accountService.withdraw(accountId, withdrawAmount);

        assertEquals(initialBalance.subtract(withdrawAmount).setScale(2, RoundingMode.HALF_EVEN), testAccount.getBalance());
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void withdraw_should_throw_exception_when_insufficient_funds() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(150.00);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

        assertThrows(IllegalArgumentException.class, () ->
                accountService.withdraw(accountId, withdrawAmount)
        );
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).save(ArgumentMatchers.any(Account.class));
    }

    @Test
    void getAccountById_should_return_account_when_account_exists() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

        Account result = accountService.getAccountById(accountId);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountById_should_throw_exception_when_account_does_not_exist() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                accountService.getAccountById(accountId)
        );
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getBalance_should_return_account_balance() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

        BigDecimal result = accountService.getBalance(accountId);

        assertEquals(initialBalance, result);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getTransactions_should_return_account_transactions() {
        List<Transaction> transactions = new ArrayList<>();
        testAccount.setTransactions(transactions);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

        List<Transaction> result = accountService.getTransactions(accountId);

        assertNotNull(result);
        assertEquals(transactions.size(), result.size());
        verify(accountRepository, times(1)).findById(accountId);
    }
}