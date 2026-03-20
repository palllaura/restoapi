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
    private final ReservationService reservationService;

    /**
     * Dining table service constructor.
     *
     * @param tableRepository DiningTableRepository.
     */
    public DiningTableService(DiningTableRepository tableRepository, ReservationService reservationService) {
        this.tableRepository = tableRepository;
        this.reservationService = reservationService;
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
     */
    private TableStatus resolveStatus(
            DiningTable table,
            LocalDateTime start,
            LocalDateTime end
    ) {
        boolean available = reservationService.isTableAvailable(
                table,
                start,
                end
        );

        return available
                ? TableStatus.FREE
                : TableStatus.OCCUPIED;
    }
}