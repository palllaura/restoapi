package com.restoapi.service;

import com.restoapi.dto.CreateReservationRequest;
import com.restoapi.entity.DiningTable;
import com.restoapi.entity.Reservation;
import com.restoapi.repository.DiningTableRepository;
import com.restoapi.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DiningTableRepository tableRepository;

    /**
     * Reservation service constructor.
     *
     * @param repository ReservationRepository.
     */
    public ReservationService(ReservationRepository repository, DiningTableRepository tableRepository) {
        this.reservationRepository = repository;
        this.tableRepository = tableRepository;
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

    /**
     * Create reservation for table if table is available.
     * @param request DTO with request data.
     */
    public void createReservation(CreateReservationRequest request) {

        DiningTable table = tableRepository.findById(request.tableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        boolean available = isTableAvailable(
                table,
                request.start(),
                request.end()
        );

        if (!available) {
            throw new RuntimeException("Table is not available");
        }

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setStartTime(request.start());
        reservation.setEndTime(request.end());
        reservation.setPartySize(request.guests());
        reservation.setCustomerName(request.customerName());

        reservationRepository.save(reservation);
    }

}