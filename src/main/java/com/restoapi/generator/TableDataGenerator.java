package com.restoapi.generator;

import com.restoapi.entity.DiningTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TableDataGenerator {

    private static final int NUM_OF_TABLES_WITH_2_SEATS = 3;
    private static final int NUM_OF_TABLES_WITH_4_SEATS = 2;
    private static final int NUM_OF_TABLES_WITH_6_SEATS = 1;

    /**
     * Generates tables according to set quantities.
     * @return generated tables in a list.
     */
    public List<DiningTable> generateTables() {
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

        return tables;
    }
}