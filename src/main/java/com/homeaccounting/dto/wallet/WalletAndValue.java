package com.homeaccounting.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Model for transferring wallet with current value.
 */
@Data
@AllArgsConstructor
public class WalletAndValue {

    private WalletForTransfer wallet;

    private BigDecimal value;
}
