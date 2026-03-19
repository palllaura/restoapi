package com.restoapi.config;

import com.restoapi.entity.DiningTable;
import com.restoapi.entity.Reservation;
import com.restoapi.repository.DiningTableRepository;
import com.restoapi.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);
    private static final int NUM_OF_TABLES_WITH_2_SEATS = 3;
    private static final int NUM_OF_TABLES_WITH_4_SEATS = 2;
    private static final int NUM_OF_TABLES_WITH_6_SEATS = 1;


    /**
     * Initialize dining tables.
     *
     * @param tableRepository DiningTableRepository.
     * @return CommandLineRunner.
     */
    @Bean
    @Order(1)
    CommandLineRunner initTables(DiningTableRepository tableRepository) {
        return args -> {

            if (tableRepository.count() == 0) {

                List<DiningTable> tables = new ArrayList<>();

                for (int i = 0; i < NUM_OF_TABLES_WITH_2_SEATS; i++) {
                    tables.add(new DiningTable(2));
                }
                for (int i = 0; i < NUM_OF_TABLES_WITH_4_SEATS; i++) {
                    tables.add(new DiningTable(4));
                }
                for (int i = 0; i < NUM_OF_TABLES_WITH_6_SEATS; i++) {
                    tables.add(new DiningTable(6));
                }
                tableRepository.saveAll(tables);
                LOGGER.info("Tables initialized, added {} tables", tables.size());
            }
        };
    }

    /**
     * Initialize random reservations.
     * @param tableRepository DiningTableRepository.
     * @param reservationRepository ReservationRepository.
     * @return CommandLineRunner.
     */
    @Bean
    @Order(2)
    CommandLineRunner initReservations(
            DiningTableRepository tableRepository,
            ReservationRepository reservationRepository
    ) {
        return args -> {

            LOGGER.info("init reservations");

            if (reservationRepository.count() == 0) {
                List<Reservation> reservations = new ArrayList<>();
                List<DiningTable> tables = tableRepository.findAll();

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

                reservationRepository.saveAll(reservations);
                LOGGER.info("Initialized {} reservations", reservations.size());
            }
        };
    }
}