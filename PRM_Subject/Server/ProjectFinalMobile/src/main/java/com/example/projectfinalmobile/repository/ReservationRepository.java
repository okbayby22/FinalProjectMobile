package com.example.projectfinalmobile.repository;

import com.example.projectfinalmobile.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
    List<Reservation> findByCustomer_CustomerId (int id);
    List<Reservation> findByCustomer_CustomerName(String name);
    List<Reservation> findByReservationDate(Date date);
    List<Reservation> findByReservationStatus(int status);
}
