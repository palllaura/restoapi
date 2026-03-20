package com.restoapi.service;

import com.restoapi.entity.DiningTable;
import com.restoapi.entity.Reservation;
import com.restoapi.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private ReservationService service;

    /**
     * Helper method to create a dining table.
     * @return DiningTable.
     */
    private DiningTable createTable() {
        DiningTable table = new DiningTable(4);
        table.setId(1L);
        return table;
    }

    /**
     * Helper method to create a reservation.
     * @param table DiningTable.
     * @param start Start time of reservation.
     * @param end End time of reservation.
     * @return Reservation.
     */
    private Reservation createReservation(
            DiningTable table,
            LocalDateTime start,
            LocalDateTime end
    ) {
        Reservation r = new Reservation();
        r.setTable(table);
        r.setStartTime(start);
        r.setEndTime(end);
        return r;
    }

    @Test
    void shouldCallRepositoryFindByTable() {
        DiningTable table = createTable();

        when(repository.findByTable(table)).thenReturn(List.of());

        service.isTableAvailable(table, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        verify(repository, times(1)).findByTable(table);
    }

    @Test
    void shouldReturnTrueWhenNoReservationsExist() {
        DiningTable table = createTable();

        when(repository.findByTable(table)).thenReturn(List.of());

        boolean result = service.isTableAvailable(
                table,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenNoOverlapExists() {
        DiningTable table = createTable();

        LocalDateTime existingStart = LocalDateTime.now().plusHours(5);
        LocalDateTime existingEnd = existingStart.plusHours(2);

        Reservation existing = createReservation(table, existingStart, existingEnd);

        when(repository.findByTable(table)).thenReturn(List.of(existing));

        boolean result = service.isTableAvailable(
                table,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenOverlapExists() {
        DiningTable table = createTable();

        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.plusHours(2);

        Reservation existing = createReservation(
                table,
                start.plusMinutes(30),
                end.plusHours(1)
        );

        when(repository.findByTable(table)).thenReturn(List.of(existing));

        boolean result = service.isTableAvailable(table, start, end);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenExistingStartsBeforeAndEndsInside() {
        DiningTable table = createTable();

        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.plusHours(2);

        Reservation existing = createReservation(
                table,
                start.minusHours(1),
                start.plusMinutes(30)
        );

        when(repository.findByTable(table)).thenReturn(List.of(existing));

        boolean result = service.isTableAvailable(table, start, end);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenReservationsTouchButDoNotOverlap() {
        DiningTable table = createTable();

        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.plusHours(2);

        Reservation existing = createReservation(
                table,
                start.minusHours(2),
                start
        );

        when(repository.findByTable(table)).thenReturn(List.of(existing));

        boolean result = service.isTableAvailable(table, start, end);

        assertTrue(result);
    }

    @Test
    void shouldHandleMultipleReservations() {
        DiningTable table = createTable();

        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.plusHours(2);

        Reservation r1 = createReservation(
                table,
                start.minusHours(4),
                start.minusHours(2)
        );

        Reservation r2 = createReservation(
                table,
                start.plusMinutes(30),
                end.plusHours(1)
        );

        when(repository.findByTable(table)).thenReturn(List.of(r1, r2));

        boolean result = service.isTableAvailable(table, start, end);

        assertFalse(result);
    }
}