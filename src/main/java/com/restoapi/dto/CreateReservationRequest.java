package com.restoapi.dto;

import java.time.LocalDateTime;

/**
 * DTO to hold reservation request data.
 * @param tableId ID of table.
 * @param start Start time.
 * @param end End time.
 * @param guests number of guests.
 * @param customerName name of customer.
 */
public record CreateReservationRequest(
        Long tableId,
        LocalDateTime start,
        LocalDateTime end,
        int guests,
        String customerName
) {}