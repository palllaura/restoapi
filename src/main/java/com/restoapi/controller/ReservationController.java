package com.restoapi.controller;

import com.restoapi.dto.CreateReservationRequest;
import com.restoapi.service.ReservationService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Reservation controller constructor.
     *
     * @param reservationService ReservationService.
     */
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Create reservation request.
     * @param request Reservation request.
     */
    @PostMapping("/reservations")
    public void createReservation(@RequestBody CreateReservationRequest request) {
        reservationService.createReservation(request);
    }

}