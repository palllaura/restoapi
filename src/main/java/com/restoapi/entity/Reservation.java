package com.restoapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {
    /**
     * Unique ID of each reservation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID of reserved table.
     */
    @ManyToOne
    @JoinColumn(name = "table_id")
    private DiningTable table;

    /**
     * Start time of reservation.
     */
    private LocalDateTime startTime;

    /**
     * End time of reservation.
     */
    private LocalDateTime endTime;

    /**
     * Number of customers.
     */
    private int partySize;

    /**
     * Name of reservation maker.
     */
    private String customerName;
}
