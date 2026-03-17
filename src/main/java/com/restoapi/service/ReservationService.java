package com.restoapi.service;

import com.restoapi.repository.ReservationRepository;
import org.springframework.stereotype.Service;

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

}