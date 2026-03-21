package com.restoapi.service;

import com.restoapi.dto.TableDto;
import com.restoapi.entity.DiningTable;
import com.restoapi.enums.TableStatus;
import com.restoapi.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
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

        List<TableDto> dtos = tableRepository.findAll().stream()
                .map(table -> mapToDto(table, startTime, endTime, partySize))
                .toList();

        return markRecommended(dtos);
    }

    /**
     * Maps DiningTable to TableDto including selectable logic.
     */
    private TableDto mapToDto(
            DiningTable table,
            LocalDateTime start,
            LocalDateTime end,
            int partySize
    ) {
        TableStatus status = resolveStatus(table, start, end);

        boolean selectable =
                status == TableStatus.FREE &&
                        table.getSeats() >= partySize;

        return new TableDto(
                table.getId(),
                table.getSeats(),
                status,
                selectable
        );
    }

    /**
     * Determines table status based on reservation availability.
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

    /**
     * Marks the most suitable table as recommended if at least one suitable table exists.
     *
     * @param tables list of tables.
     * @return list of tables as dtos.
     */
    private List<TableDto> markRecommended(List<TableDto> tables) {

        TableDto best = tables.stream()
                .filter(TableDto::selectable)
                .min(Comparator.comparingInt(TableDto::seats))
                .orElse(null);

        if (best == null) {
            return tables;
        }

        return tables.stream()
                .map(t -> new TableDto(
                        t.id(),
                        t.seats(),
                        t.id().equals(best.id())
                                ? TableStatus.RECOMMENDED
                                : t.status(),
                        t.selectable()
                ))
                .toList();
    }
}