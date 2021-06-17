package com.example.projectfinalmobile.controller;

import com.example.projectfinalmobile.entity.Reservation;
import com.example.projectfinalmobile.service.ReservationService;
import com.example.projectfinalmobile.service.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/reservation/api")
public class ReservationController {

    @Autowired
    private ReservationServiceImpl service;

    @GetMapping("/{id}")
    public List<Reservation> listReservationByCustomerId(@PathVariable ("id") int customerid){
        return service.listReservationByCustomerId(customerid);
    }

    @PostMapping("/{date}/add")
    public Reservation addReservation(@RequestBody Reservation reservation, @PathVariable ("date")String date){
        return service.addReservation(reservation, date);
    }

    @PutMapping("/{id}/updatecustomer")
    public Reservation editReservationForCustomer(@PathVariable ("id") int id,@RequestBody Reservation reservationUpdate){
        return service.editReservationForCustomer(id,reservationUpdate);
    }

    @PutMapping("/{id}/{status}/updatestatus")
    public Reservation editReservationForStaff(@PathVariable ("id") int id,@PathVariable ("status") int status){
        return service.editReservationForStaff(id, status);
    }

    @GetMapping("/{name}/findbyname")
    public List<Reservation> findByCustomerName(@PathVariable("name") String name){
        return service.findByCustomerName(name);
    }

    @GetMapping("/{date}/findbydate")
    public List<Reservation> findByReservationDate(@PathVariable("date") Date date){
        return service.findByReservationDate(date);
    }

    @GetMapping("/{status}/findbystatus")
    public List<Reservation> findByReservationStatus(@PathVariable("status") int status){
        return service.findByReservationStatus(status);
    }
}
