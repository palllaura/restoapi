package com.restoapi.generator;

import com.restoapi.entity.DiningTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.restoapi.constants.TableConstants.NUM_OF_TABLES_WITH_2_SEATS;
import static com.restoapi.constants.TableConstants.NUM_OF_TABLES_WITH_4_SEATS;
import static com.restoapi.constants.TableConstants.NUM_OF_TABLES_WITH_6_SEATS;

@Component
public class TableDataGenerator {

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