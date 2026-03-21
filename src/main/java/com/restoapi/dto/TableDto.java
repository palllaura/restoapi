package com.restoapi.dto;

import com.restoapi.enums.TableStatus;

/**
 * Table DTO for displaying dining tables.
 * @param id unique id of table.
 * @param seats number of seats.
 * @param status table status.
 * @param selectable is table selectable for reservation.
 */
public record TableDto(
        Long id,
        int seats,
        TableStatus status,
        boolean selectable
) {}
