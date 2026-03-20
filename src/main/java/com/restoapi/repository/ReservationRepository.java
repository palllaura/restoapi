package com.restoapi.repository;

import com.restoapi.entity.DiningTable;
import com.restoapi.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTable(DiningTable table);
}
