package com.homeaccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model for transferring group.
 */
@Data
@AllArgsConstructor
public class GroupForTransfer {

    private long id;

    private String type;
}
