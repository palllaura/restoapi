package com.restoapi.dto;

/**
 * Response for reservation creation.
 */
public record ReservationResponse(
        boolean success,
        String message
) {}