package io.bank;

import io.bank.domain.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
class BankApplicationTests {

    Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void deposit_should_increase_balance() {
        BigDecimal depositAmount = BigDecimal.valueOf(100);

        account.deposit(depositAmount);

        BigDecimal newBalance = account.getBalance();
        Assertions.assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_EVEN), newBalance);
    }

    @Test
    void deposit_should_throw_exception_when_amount_is_zero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(BigDecimal.ZERO);
        });

    }

    @Test
    void deposit_should_throw_exception_when_amount_is_negative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(BigDecimal.valueOf(-100));
        });
    }

    @Test
    void deposit_should_round_to_two_decimals() {
        BigDecimal depositAmount = BigDecimal.valueOf(100.456);
        BigDecimal expectedBalance = BigDecimal.valueOf(100.46);

        account.deposit(depositAmount);

        BigDecimal newBalance = account.getBalance();
        Assertions.assertEquals(expectedBalance, newBalance);

    }

    @Test
    void withdraw_should_decrease_balance() {
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        BigDecimal withdrawAmount = BigDecimal.valueOf(50);

        account.deposit(depositAmount);
        account.withdraw(withdrawAmount);

        BigDecimal newBalance = account.getBalance();
        Assertions.assertEquals(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_EVEN), newBalance);
    }

    @Test
    void withdraw_should_throw_exception_when_amount_is_zero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(BigDecimal.ZERO);
        });
    }

    @Test
    void withdraw_should_throw_exception_when_amount_is_negative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(BigDecimal.valueOf(-100));
        });
    }

    @Test
    void withdraw_should_throw_exception_when_insufficient_funds() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(BigDecimal.valueOf(100));
        });
    }

    @Test
    void withdraw_should_round_to_two_decimals() {
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        BigDecimal withdrawAmount = BigDecimal.valueOf(50.678);

        account.deposit(depositAmount);
        account.withdraw(withdrawAmount);
        BigDecimal newBalance = account.getBalance();

        Assertions.assertEquals(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(50.68)), newBalance);
    }

    @Test
    void getTransactions_should_return_all_transactions() {
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        account.deposit(depositAmount);
        BigDecimal withdrawAmount = BigDecimal.valueOf(50);
        account.withdraw(withdrawAmount);

        Assertions.assertEquals(2, account.getTransactions().size());
    }


}
