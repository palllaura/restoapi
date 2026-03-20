package com.restoapi.constants;

/**
 * Holds reservation related constants.
 */
public class ReservationConstants {

    public static final int OPENING_HOUR = 10;
    public static final int CLOSING_HOUR = 22;

    public static final int MIN_RESERVATION_DURATION_HOURS = 1;
    public static final int MAX_RESERVATION_DURATION_HOURS = 3;

    public static final int MAX_DAYS_IN_FUTURE = 3;

    public static final int MAX_RESERVATIONS_PER_TABLE = 3;
    public static final int MAX_GENERATION_ATTEMPTS = 10;

    private ReservationConstants() {
    }
}