package io.bank.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(LocalDate date, String amount, BigDecimal balance) {
}
