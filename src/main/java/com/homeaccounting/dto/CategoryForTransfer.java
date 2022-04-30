package com.homeaccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model for transferring category.
 */
@Data
@AllArgsConstructor
public class CategoryForTransfer {

    private long id;

    private String name;

    private String icon;

    private GroupForTransfer group;
}
