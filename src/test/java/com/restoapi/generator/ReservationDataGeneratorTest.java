package com.restoapi.generator;

import com.restoapi.constants.ReservationConstants;
import com.restoapi.entity.DiningTable;
import com.restoapi.entity.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ReservationDataGeneratorTest {

    private final ReservationDataGenerator generator = new ReservationDataGenerator();

    /**
     * Helper method to create test data.
     */
    private List<DiningTable> createTables() {
        return List.of(
                new DiningTable(2),
                new DiningTable(4),
                new DiningTable(6)
        );
    }

    @Test
    void shouldGenerateReservations() {
        List<Reservation> reservations = generator.generateReservations(createTables());

        Assertions.assertNotNull(reservations);
        Assertions.assertFalse(reservations.isEmpty());
    }

    @Test
    void shouldAssignReservationToTable() {
        List<Reservation> reservations = generator.generateReservations(createTables());

        Assertions.assertTrue(
                reservations.stream().allMatch(r -> r.getTable() != null)
        );
    }

    @Test
    void shouldHaveValidTimeRanges() {
        List<Reservation> reservations = generator.generateReservations(createTables());

        Assertions.assertTrue(
                reservations.stream()
                        .allMatch(r -> r.getStartTime().isBefore(r.getEndTime()))
        );
    }

    @Test
    void shouldRespectWorkingHours() {
        List<Reservation> reservations = generator.generateReservations(createTables());

        Assertions.assertTrue(
                reservations.stream().allMatch(r ->
                        r.getStartTime().getHour() >= ReservationConstants.OPENING_HOUR &&
                                r.getEndTime().getHour() <= ReservationConstants.CLOSING_HOUR
                )
        );
    }

    @Test
    void shouldRespectReservationDurationLimits() {
        List<Reservation> reservations = generator.generateReservations(createTables());

        Assertions.assertTrue(
                reservations.stream().allMatch(r -> {
                    long duration = java.time.Duration.between(
                            r.getStartTime(),
                            r.getEndTime()
                    ).toHours();

                    return duration >= ReservationConstants.MIN_RESERVATION_DURATION_HOURS &&
                            duration <= ReservationConstants.MAX_RESERVATION_DURATION_HOURS;
                })
        );
    }

    @Test
    void shouldNotHaveOverlappingReservationsPerTable() {
        List<DiningTable> tables = createTables();
        List<Reservation> reservations = generator.generateReservations(tables);

        for (DiningTable table : tables) {

            List<Reservation> tableReservations = reservations.stream()
                    .filter(r -> r.getTable() == table)
                    .toList();

            for (int i = 0; i < tableReservations.size(); i++) {
                for (int j = i + 1; j < tableReservations.size(); j++) {

                    boolean overlap = isOverlapping(
                            tableReservations.get(i),
                            tableReservations.get(j)
                    );

                    Assertions.assertFalse(
                            overlap,
                            "Found overlapping reservations for same table"
                    );
                }
            }
        }
    }

    private boolean isOverlapping(Reservation a, Reservation b) {
        return a.getStartTime().isBefore(b.getEndTime()) &&
                b.getStartTime().isBefore(a.getEndTime());
    }
}