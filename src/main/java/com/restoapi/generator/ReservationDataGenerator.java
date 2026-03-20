package com.restoapi.generator;

import com.restoapi.constants.ReservationConstants;
import com.restoapi.entity.DiningTable;
import com.restoapi.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ReservationDataGenerator {

    private final Random random = new Random();

    /**
     * Generates random reservations to a given list of dining tables.
     * @param tables list of dining tables.
     * @return list of reservations.
     */
    public List<Reservation> generateReservations(List<DiningTable> tables) {
        List<Reservation> reservations = new ArrayList<>();

        for (DiningTable table : tables) {
            reservations.addAll(generateReservationsForTable(table));
        }

        return reservations;
    }

    /**
     * Generates reservations for a single table.
     */
    private List<Reservation> generateReservationsForTable(DiningTable table) {
        List<Reservation> tableReservations = new ArrayList<>();

        int numReservations = random.nextInt(
                ReservationConstants.MAX_RESERVATIONS_PER_TABLE + 1
        );

        for (int i = 0; i < numReservations; i++) {
            tryCreateReservation(table, tableReservations);
        }

        return tableReservations;
    }

    /**
     * Attempts to create a single reservation without overlapping.
     */
    private void tryCreateReservation(
            DiningTable table,
            List<Reservation> tableReservations
    ) {
        int attempts = 0;

        while (attempts < ReservationConstants.MAX_GENERATION_ATTEMPTS) {

            LocalDateTime start = generateStartTime();
            int duration = generateDuration();
            LocalDateTime end = start.plusHours(duration);

            if (!isOverlappingAny(start, end, tableReservations)) {
                tableReservations.add(
                        createReservation(table, start, end)
                );
                return;
            }

            attempts++;
        }
    }

    /**
     * Generates valid start time within working hours.
     */
    private LocalDateTime generateStartTime() {
        int latestStartHour = ReservationConstants.CLOSING_HOUR
                - ReservationConstants.MAX_RESERVATION_DURATION_HOURS;

        int hour = ReservationConstants.OPENING_HOUR +
                random.nextInt(latestStartHour - ReservationConstants.OPENING_HOUR + 1);

        return LocalDateTime.now()
                .plusDays(random.nextInt(ReservationConstants.MAX_DAYS_IN_FUTURE))
                .withHour(hour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * Generates random reservation duration.
     */
    private int generateDuration() {
        return ReservationConstants.MIN_RESERVATION_DURATION_HOURS +
                random.nextInt(
                        ReservationConstants.MAX_RESERVATION_DURATION_HOURS
                                - ReservationConstants.MIN_RESERVATION_DURATION_HOURS + 1
                );
    }

    /**
     * Creates reservation entity.
     */
    private Reservation createReservation(
            DiningTable table,
            LocalDateTime start,
            LocalDateTime end
    ) {
        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setCustomerName("Customer Name");
        reservation.setPartySize(table.getSeats());
        return reservation;
    }

    /**
     * Checks overlap against all existing reservations for a table.
     */
    private boolean isOverlappingAny(
            LocalDateTime start,
            LocalDateTime end,
            List<Reservation> existingReservations
    ) {
        return existingReservations.stream()
                .anyMatch(r -> isOverlapping(start, end, r));
    }

    /**
     * Checks if two time ranges overlap.
     */
    private boolean isOverlapping(
            LocalDateTime start,
            LocalDateTime end,
            Reservation existing
    ) {
        return start.isBefore(existing.getEndTime()) &&
                existing.getStartTime().isBefore(end);
    }
}