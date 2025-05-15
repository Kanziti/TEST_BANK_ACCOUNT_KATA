package io.bank.controller;

import io.bank.domain.Transaction;
import io.bank.domain.TransactionType;
import io.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit(@RequestParam long accountId, @RequestParam BigDecimal amount) {
        accountService.deposit(accountId, amount);
        return ResponseEntity.ok(toAccountDTO(accountId));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@RequestParam long accountId, @RequestParam BigDecimal amount) {
        accountService.withdraw(accountId, amount);
        return ResponseEntity.ok(toAccountDTO(accountId));
    }

    @GetMapping("/statement")
    public ResponseEntity<AccountDTO> getStatement(@RequestParam long accountId) {
        return ResponseEntity.ok(toAccountDTO(accountId));
    }

    private AccountDTO toAccountDTO(@RequestParam long accountId) {
        return new AccountDTO(
                accountService.getBalance(accountId),
                accountService.getTransactions(accountId).stream()
                        .map(t -> new TransactionDTO(
                                t.getDate(),
                                formatSignedAmount(t),
                                t.getBalanceAfter()))
                        .toList()
                        .reversed());
    }

    private static String formatSignedAmount(Transaction t) {
        return switch (t.getType()) {
            case TransactionType.DEPOSIT -> "+" + t.getAmount().toPlainString();
            case TransactionType.WITHDRAWAL -> "-" + t.getAmount().toPlainString();
        };
    }

}
