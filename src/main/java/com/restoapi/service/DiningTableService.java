package com.restoapi.service;

import com.restoapi.dto.TableDto;
import com.restoapi.enums.TableStatus;
import com.restoapi.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DiningTableService {
    private final DiningTableRepository tableRepository;

    /**
     * Dining table service constructor.
     *
     * @param tableRepository DiningTableRepository.
     */
    public DiningTableService(DiningTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    /**
     * Get all dining tables from database.
     * @return all tables in a list.
     */
    public List<TableDto> getTables() {
        return tableRepository.findAll().stream()
                .map(table -> new TableDto(
                        table.getId(),
                        table.getSeats(),
                        TableStatus.FREE
                ))
                .toList();
    }

}
