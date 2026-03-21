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
    void shouldMapTablesWithCorrectStatusesWithoutRecommendation() {
        when(repository.findAll()).thenReturn(List.of(table2, table4));

        when(reservationService.isTableAvailable(eq(table2), any(), any()))
                .thenReturn(true);

        when(reservationService.isTableAvailable(eq(table4), any(), any()))
                .thenReturn(false);

        List<TableDto> result = service.getTables(null, null, 5);

        assertEquals(TableStatus.FREE, result.get(0).status());
        assertEquals(TableStatus.OCCUPIED, result.get(1).status());
    }

    @Test
    void shouldMarkBestFreeTableAsRecommended() {
        when(repository.findAll()).thenReturn(List.of(table2, table4, table6));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, 3);

        TableDto table2Dto = result.get(0);
        TableDto table4Dto = result.get(1);
        TableDto table6Dto = result.get(2);

        assertEquals(TableStatus.FREE, table2Dto.status());
        assertEquals(TableStatus.RECOMMENDED, table4Dto.status());
        assertEquals(TableStatus.FREE, table6Dto.status());
    }

    @Test
    void shouldRecommendSmallestSuitableFreeTable() {
        when(repository.findAll()).thenReturn(List.of(table2, table4, table6));
        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, 4);

        TableDto recommended = result.stream()
                .filter(t -> t.status() == TableStatus.RECOMMENDED)
                .findFirst()
                .orElse(null);

        assertNotNull(recommended);
        assertEquals(4, recommended.seats());
    }

    @Test
    void shouldNotRecommendTooSmallTables() {
        when(repository.findAll()).thenReturn(List.of(table2, table4));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, 4);

        assertTrue(result.stream()
                .filter(t -> t.seats() == 2)
                .noneMatch(t -> t.status() == TableStatus.RECOMMENDED));
    }

    @Test
    void shouldNotRecommendOccupiedTables() {
        when(repository.findAll()).thenReturn(List.of(table4, table6));

        when(reservationService.isTableAvailable(eq(table4), any(), any()))
                .thenReturn(false);

        when(reservationService.isTableAvailable(eq(table6), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, 4);

        TableDto recommended = result.stream()
                .filter(t -> t.status() == TableStatus.RECOMMENDED)
                .findFirst()
                .orElse(null);

        assertNotNull(recommended);
        assertEquals(6, recommended.seats());
    }

    @Test
    void shouldReturnNoRecommendedIfNoSuitableTables() {
        when(repository.findAll()).thenReturn(List.of(table2));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, 4);

        assertTrue(result.stream()
                .noneMatch(t -> t.status() == TableStatus.RECOMMENDED));
    }

    @Test
    void shouldReturnNoRecommendedIfAllTablesOccupied() {
        when(repository.findAll()).thenReturn(List.of(table4, table6));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(false);

        List<TableDto> result = service.getTables(null, null, 4);

        assertTrue(result.stream()
                .noneMatch(t -> t.status() == TableStatus.RECOMMENDED));
    }

    @Test
    void shouldPassCorrectParametersToReservationService() {
        when(repository.findAll()).thenReturn(List.of(table2));

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        service.getTables(start, end, 2);

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

    @Test
    void shouldMarkTableSelectableWhenFreeAndBigEnough() {
        when(repository.findAll()).thenReturn(List.of(table4));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, 3);

        assertTrue(result.getFirst().selectable());
    }

    @Test
    void shouldNotBeSelectableWhenTableTooSmall() {
        when(repository.findAll()).thenReturn(List.of(table2));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(true);

        List<TableDto> result = service.getTables(null, null, 4);

        assertFalse(result.getFirst().selectable());
    }

    @Test
    void shouldNotBeSelectableWhenTableOccupied() {
        when(repository.findAll()).thenReturn(List.of(table6));

        when(reservationService.isTableAvailable(any(), any(), any()))
                .thenReturn(false);

        List<TableDto> result = service.getTables(null, null, 4);

        assertFalse(result.getFirst().selectable());
    }
}