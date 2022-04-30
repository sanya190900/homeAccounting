package com.homeaccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Model for transferring amount value for specified date time.
 */
@Data
@AllArgsConstructor
public class AmountByDate {

    private Instant datetime;

    private BigDecimal value;
}
