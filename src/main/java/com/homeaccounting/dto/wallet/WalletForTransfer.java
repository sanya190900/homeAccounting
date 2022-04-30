package com.homeaccounting.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model for transferring wallet.
 */
@Data
@AllArgsConstructor
public class WalletForTransfer {

    private long id;

    private String name;

    private String currency;
}
