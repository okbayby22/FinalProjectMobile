package com.example.projectfinalmobile.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;

@Entity
public class Reservation {

    @Id
    private int reservationId;
    private Time reservationTime;
    private int reservationStatus;
    private int staffId;
    private int deskId;
    private int numberOfPeople;
    private int discountCode;

    public Reservation() {
    }

    public Reservation(int reservationId, Time reservationTime, int reservationStatus, int staffId, int deskId, int numberOfPeople, int discountCode) {
        this.reservationId = reservationId;
        this.reservationTime = reservationTime;
        this.reservationStatus = reservationStatus;
        this.staffId = staffId;
        this.deskId = deskId;
        this.numberOfPeople = numberOfPeople;
        this.discountCode = discountCode;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Time getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Time reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(int reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public int getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(int discountCode) {
        this.discountCode = discountCode;
    }
}
