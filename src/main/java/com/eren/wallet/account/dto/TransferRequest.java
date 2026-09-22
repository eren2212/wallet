package com.eren.wallet.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(UUID toAccountId, BigDecimal amount) {

}
