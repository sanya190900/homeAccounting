package com.homeaccounting.dto;

import com.homeaccounting.dto.wallet.WalletForTransfer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Model for transferring transactions.
 */
@Data
@AllArgsConstructor
public class TransactionForTransfer {

    private long id;

    private Instant date;

    private String description;

    private String photo;

    private BigDecimal value;

    private CategoryForTransfer category;

    private WalletForTransfer wallet;
}
