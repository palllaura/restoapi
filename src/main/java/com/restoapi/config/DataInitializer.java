package com.restoapi.config;

import com.restoapi.generator.ReservationDataGenerator;
import com.restoapi.generator.TableDataGenerator;
import com.restoapi.repository.DiningTableRepository;
import com.restoapi.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Initialize dining tables.
     *
     * @param tableRepository DiningTableRepository.
     * @return CommandLineRunner.
     */
    @Bean
    @Order(1)
    CommandLineRunner initTables(
            DiningTableRepository tableRepository,
            TableDataGenerator tableGenerator
    ) {
        return args -> {

            if (tableRepository.count() == 0) {
                var tables = tableGenerator.generateTables();
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
            ReservationRepository reservationRepository,
            ReservationDataGenerator reservationGenerator
    ) {
        return args -> {

            if (reservationRepository.count() == 0) {

                var tables = tableRepository.findAll();
                var reservations = reservationGenerator.generateReservations(tables);

                reservationRepository.saveAll(reservations);

                LOGGER.info("Initialized {} reservations", reservations.size());
            }
        };
    }
}