package io.bank.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    private static final String INVALID_AMOUNT = "Amount must be greater than zero";
    private static final String INSUFFICIENT_FUNDS = "Insufficient funds";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();

    public synchronized void deposit(BigDecimal amount) {
        validateAmount(amount);
        BigDecimal depositAmount = setDecimalsRounding(amount);
        balance = setDecimalsRounding(balance.add(depositAmount));
        recordTransaction(TransactionType.DEPOSIT, depositAmount, balance);
    }

    public synchronized void withdraw(BigDecimal amount) {
        validateAmount(amount);
        BigDecimal withdrawAmount = setDecimalsRounding(amount);

        if (balance.compareTo(withdrawAmount) < 0) {
            throw new IllegalArgumentException(INSUFFICIENT_FUNDS);
        }

        balance = setDecimalsRounding(balance.subtract(withdrawAmount));
        recordTransaction(TransactionType.WITHDRAWAL, withdrawAmount, balance);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }
    }

    private static BigDecimal setDecimalsRounding(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    private void recordTransaction(TransactionType type, BigDecimal amount, BigDecimal balance) {
        transactions.add(Transaction.builder()
                .account(this)
                .type(type)
                .date(LocalDate.now())
                .amount(amount)
                .balanceAfter(balance)
                .build());
    }
}
