package com.restoapi.service;

import com.restoapi.entity.DiningTable;
import com.restoapi.entity.Reservation;
import com.restoapi.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * Reservation service constructor.
     *
     * @param repository ReservationRepository.
     */
    public ReservationService(ReservationRepository repository) {
        this.reservationRepository = repository;
    }

    /**
     * Check if table is available during given time.
     * @param table Dining table to check.
     * @param start start of time.
     * @param end end of time.
     * @return true if table is available, false if table is booked.
     */
    public boolean isTableAvailable(
            DiningTable table,
            LocalDateTime start,
            LocalDateTime end
    ) {
        List<Reservation> reservations =
                reservationRepository.findByTable(table);

        return reservations.stream()
                .noneMatch(r -> isOverlapping(start, end, r));
    }

    /**
     * Determine given time overlaps with an existing reservation.
     * @param start start time to check.
     * @param end end time to check.
     * @param existing existing reservation.
     * @return true if overlaps, else false.
     */
    private boolean isOverlapping(
            LocalDateTime start,
            LocalDateTime end,
            Reservation existing
    ) {
        return start.isBefore(existing.getEndTime()) &&
                existing.getStartTime().isBefore(end);
    }

}