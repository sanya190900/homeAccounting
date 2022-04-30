package com.homeaccounting.dto;

import com.homeaccounting.dao.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Model for transferring amount value in specified category.
 */
@Data
@AllArgsConstructor
public class AmountByCategory {

    private Category category;

    private BigDecimal value;
}
