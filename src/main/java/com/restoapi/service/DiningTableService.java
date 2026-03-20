package com.restoapi.service;

import com.restoapi.dto.TableDto;
import com.restoapi.entity.DiningTable;
import com.restoapi.enums.TableStatus;
import com.restoapi.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * Get all dining tables with optional filtering.
     *
     * @param start  reservation start time (optional)
     * @param end    reservation end time (optional)
     * @param guests number of guests (optional)
     * @return list of table DTOs
     */
    public List<TableDto> getTables(
            LocalDateTime start,
            LocalDateTime end,
            Integer guests
    ) {

        LocalDateTime startTime = (start == null)
                ? LocalDateTime.now().plusHours(1)
                : start;

        LocalDateTime endTime = (end == null)
                ? startTime.plusHours(2)
                : end;

        int partySize = (guests == null)
                ? 2
                : guests;

        return tableRepository.findAll().stream()
                .map(table -> new TableDto(
                        table.getId(),
                        table.getSeats(),
                        resolveStatus(table, startTime, endTime)
                ))
                .toList();
    }

    /**
     * Determines table status.
     * Currently, returns FREE for all tables.
     * Next step: integrate reservation logic.
     */
    private TableStatus resolveStatus(
            DiningTable table,
            LocalDateTime start,
            LocalDateTime end
    ) {
        // TODO: check reservations here
        return TableStatus.FREE;
    }
}