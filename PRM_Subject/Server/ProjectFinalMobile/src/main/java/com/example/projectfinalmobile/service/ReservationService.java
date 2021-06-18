package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.entity.Reservation;

import java.sql.Date;
import java.util.List;

public interface ReservationService {
    List<Reservation> listReservationByCustomerId(int customerid);
    Reservation addReservation(Reservation reservation,String date);
    Reservation editReservationForCustomer(int id,String date, Reservation reservationUpdate);
    Reservation editReservationForStaff(int id,int status);
    List<Reservation> findByCustomerName(String name);
    List<Reservation> findByReservationDate(Date date);
    List<Reservation> findByReservationStatus(int status);
}
