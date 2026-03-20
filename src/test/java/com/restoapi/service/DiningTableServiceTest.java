package com.restoapi.service;

import com.restoapi.dto.TableDto;
import com.restoapi.entity.DiningTable;
import com.restoapi.repository.DiningTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiningTableServiceTest {

    @Mock
    private DiningTableRepository repository;

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
    void shouldReturnAllTablesWhenGuestsIsNull() {
        when(repository.findAll()).thenReturn(List.of(table2, table4, table6));

        List<TableDto> result = service.getTables(null, null, null);

        assertEquals(3, result.size());
    }

    @Test
    void shouldMapTablesToDtosCorrectly() {
        when(repository.findAll()).thenReturn(List.of(table4));

        List<TableDto> result = service.getTables(null, null, null);
        TableDto dto = result.getFirst();

        assertEquals(table4.getId(), dto.id());
        assertEquals(table4.getSeats(), dto.seats());
    }

    @Test
    void shouldNotModifyOriginalEntities() {
        when(repository.findAll()).thenReturn(List.of(table2));

        service.getTables(null, null, null);

        assertEquals(2, table2.getSeats());
    }
}