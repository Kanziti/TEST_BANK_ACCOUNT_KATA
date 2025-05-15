package io.bank.controller;

import java.math.BigDecimal;
import java.util.List;

public record AccountDTO(BigDecimal balance, List<TransactionDTO> transactions) {
}
