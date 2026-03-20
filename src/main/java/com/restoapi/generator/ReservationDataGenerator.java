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

    /**
     * Generates random reservations to a given list of dining tables.
     * @param tables list of dining tables.
     * @return list of reservations.
     */
    public List<Reservation> generateReservations(List<DiningTable> tables) {
        List<Reservation> reservations = new ArrayList<>();
        Random random = new Random();

        for (DiningTable table : tables) {

            List<Reservation> tableReservations = new ArrayList<>();

            int numReservations = random.nextInt(
                    ReservationConstants.MAX_RESERVATIONS_PER_TABLE + 1
            );

            for (int i = 0; i < numReservations; i++) {

                int attempts = 0;
                boolean created = false;

                while (attempts < ReservationConstants.MAX_GENERATION_ATTEMPTS && !created) {

                    int hour = ReservationConstants.OPENING_HOUR +
                            random.nextInt(
                                    ReservationConstants.CLOSING_HOUR
                                            - ReservationConstants.OPENING_HOUR
                            );

                    LocalDateTime start = LocalDateTime.now()
                            .plusDays(random.nextInt(ReservationConstants.MAX_DAYS_IN_FUTURE))
                            .withHour(hour)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0);

                    int durationHours =
                            ReservationConstants.MIN_RESERVATION_DURATION_HOURS +
                                    random.nextInt(
                                            ReservationConstants.MAX_RESERVATION_DURATION_HOURS
                                                    - ReservationConstants.MIN_RESERVATION_DURATION_HOURS + 1
                                    );

                    LocalDateTime end = start.plusHours(durationHours);

                    boolean overlaps = tableReservations.stream()
                            .anyMatch(r -> isOverlapping(start, end, r));

                    if (!overlaps) {
                        Reservation reservation = new Reservation();
                        reservation.setTable(table);
                        reservation.setStartTime(start);
                        reservation.setEndTime(end);
                        reservation.setCustomerName("Customer Name");
                        reservation.setPartySize(table.getSeats());

                        tableReservations.add(reservation);
                        reservations.add(reservation);

                        created = true;
                    }

                    attempts++;
                }
            }
        }

        return reservations;
    }

    /**
     * Helper method to check if chosen time overlaps with already existing reservation.
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