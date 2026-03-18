package com.restoapi.config;

import com.restoapi.entity.DiningTable;
import com.restoapi.repository.DiningTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
}