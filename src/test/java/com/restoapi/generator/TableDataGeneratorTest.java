package com.restoapi.generator;

import com.restoapi.entity.DiningTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.restoapi.constants.TableConstants.NUM_OF_TABLES_WITH_2_SEATS;
import static com.restoapi.constants.TableConstants.NUM_OF_TABLES_WITH_4_SEATS;
import static com.restoapi.constants.TableConstants.NUM_OF_TABLES_WITH_6_SEATS;

class TableDataGeneratorTest {

    private final TableDataGenerator generator = new TableDataGenerator();

    @Test
    void shouldGenerateCorrectNumberOfTables() {
        List<DiningTable> tables = generator.generateTables();
        int expected =
                NUM_OF_TABLES_WITH_2_SEATS +
                        NUM_OF_TABLES_WITH_4_SEATS +
                        NUM_OF_TABLES_WITH_6_SEATS;

        Assertions.assertEquals(expected, tables.size());
    }

    @Test
    void shouldGenerateTablesWithCorrectSeatCounts() {
        List<DiningTable> tables = generator.generateTables();

        long seats2 = tables.stream().filter(t -> t.getSeats() == 2).count();
        long seats4 = tables.stream().filter(t -> t.getSeats() == 4).count();
        long seats6 = tables.stream().filter(t -> t.getSeats() == 6).count();

        Assertions.assertEquals(NUM_OF_TABLES_WITH_2_SEATS, seats2);
        Assertions.assertEquals(NUM_OF_TABLES_WITH_4_SEATS, seats4);
        Assertions.assertEquals(NUM_OF_TABLES_WITH_6_SEATS, seats6);
    }
}