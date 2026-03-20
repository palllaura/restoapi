package com.restoapi.service;

import com.restoapi.dto.TableDto;
import com.restoapi.entity.DiningTable;
import com.restoapi.enums.TableStatus;
import com.restoapi.repository.DiningTableRepository;
import org.junit.jupiter.api.BeforeEach;
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
class DiningTableServiceTest {

    @Mock
    private DiningTableRepository repository;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private DiningTableService service;

    private DiningTable table2;
    private DiningTable table4;
    private DiningTable table6;

    @BeforeEach
    void setUp() {
        table2 = new DiningTable(2);
        table2.setId(1L);

        table4 = new DiningTable(4);
        table4.setId(2L);

        table6 = new DiningTable(6);
        table6.setId(3L);
    }

    @Test
    void shouldCallRepositoryFindAll() {
        when(repository.findAll()).thenReturn(List.of());

        service.getTables(null, null, null);

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoTablesExist() {
        when(repository.findAll()).thenReturn(List.of());

        List<TableDto> result = service.getTables(null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllTables() {
        when(repository.findAll()).thenReturn(List.of(table2, table4, table6));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, null);

        assertEquals(3, result.size());
    }

    @Test
    void shouldMapTablesToDtosCorrectly() {
        when(repository.findAll()).thenReturn(List.of(table4));
        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, null);
        TableDto dto = result.getFirst();

        assertEquals(table4.getId(), dto.id());
        assertEquals(table4.getSeats(), dto.seats());
        assertEquals(TableStatus.FREE, dto.status());
    }

    @Test
    void shouldReturnOccupiedWhenTableIsNotAvailable() {
        when(repository.findAll()).thenReturn(List.of(table2));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(false);

        List<TableDto> result = service.getTables(null, null, null);

        assertEquals(TableStatus.OCCUPIED, result.getFirst().status());
    }

    @Test
    void shouldReturnFreeWhenTableIsAvailable() {
        when(repository.findAll()).thenReturn(List.of(table2));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, null);

        assertEquals(TableStatus.FREE, result.getFirst().status());
    }

    @Test
    void shouldPassCorrectParametersToReservationService() {
        when(repository.findAll()).thenReturn(List.of(table2));

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        service.getTables(start, end, 4);

        verify(reservationService, times(1))
                .isTableAvailable(table2, start, end);
    }

    @Test
    void shouldNotModifyOriginalEntities() {
        when(repository.findAll()).thenReturn(List.of(table2));
        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        service.getTables(null, null, null);

        assertEquals(2, table2.getSeats());
    }
}