package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.entity.Reservation;
import com.example.projectfinalmobile.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    private ReservationRepository reserRepo;

    public List<Reservation> listReservationByCustomerId(int customerid){
        return reserRepo.findByCustomer_CustomerId(customerid);
    }

    public Reservation addReservation(Reservation reservation){
        return reserRepo.save(reservation);
    }

    public Reservation editReservationForCustomer(int id, Reservation reservationUpdate){
        Reservation newreservation = reserRepo.getById(id);
        newreservation.setReservationDate(reservationUpdate.getReservationDate());
        newreservation.setReservationTime(reservationUpdate.getReservationTime());
        newreservation.setNumberOfTickets(reservationUpdate.getNumberOfTickets());
        newreservation.setReservationAmount(reservationUpdate.getReservationAmount());
        newreservation.setReservationStatus(0);
        return reserRepo.save(newreservation);
    }

    public Reservation editReservationForStaff(int id,int status){
        Reservation newreservation = reserRepo.getById(id);
        newreservation.setReservationStatus(status);
        return  reserRepo.save(newreservation);
    }

    public List<Reservation> findByCustomerName(String name){
        return reserRepo.findByCustomer_CustomerName(name);
    }

    public List<Reservation> findByReservationDate(Date date){
        return reserRepo.findByReservationDate(date);
    }

    public List<Reservation> findByReservationStatus(int status){
        return reserRepo.findByReservationStatus(status);
    }
}
