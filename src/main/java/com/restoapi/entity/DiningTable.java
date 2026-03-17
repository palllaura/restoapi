package com.restoapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "diningtable")
public class DiningTable {
    /**
     * Unique ID of each table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Number of seats.
     */
    private int seats;

    /**
     * No-args constructor.
     */
    public DiningTable() {
    }

    /**
     * Dining table constructor with number of seats.
     * @param seats number of seats.
     */
    public DiningTable(int seats) {
        this.seats = seats;
    }
}
