package com.restoapi.generator;

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

            int numReservations = random.nextInt(3);

            for (int i = 0; i < numReservations; i++) {

                int hour = 10 + random.nextInt(12);

                LocalDateTime start = LocalDateTime.now()
                        .plusDays(random.nextInt(3))
                        .withHour(hour)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);

                int durationHours = 1 + random.nextInt(3);
                LocalDateTime end = start.plusHours(durationHours);

                Reservation reservation = new Reservation();
                reservation.setTable(table);
                reservation.setStartTime(start);
                reservation.setEndTime(end);
                reservation.setCustomerName("Customer Name");
                reservation.setPartySize(table.getSeats());

                reservations.add(reservation);
            }
        }

        return reservations;
    }
}