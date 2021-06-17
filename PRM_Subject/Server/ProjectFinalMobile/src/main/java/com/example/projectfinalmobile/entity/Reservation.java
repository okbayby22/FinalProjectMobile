package com.example.projectfinalmobile.entity;

import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    private Date reservationDate;

    @Column(length = 30)
    private String reservationTime;

    @Column(length = 1)
    private int reservationStatus;

    @Column(length = 10)
    private int deskId;

    @Column(length = 2)
    private int numberOfTickets;

    private double reservationAmount;

    @OneToMany(mappedBy = "reservations", cascade = CascadeType.ALL)
    private List<ReservationDesks> reservationDesks;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "discountId")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private Staff staff;

    public Reservation() {
    }

    public Reservation(int reservationId, Date reservationDate, String reservationTime, int reservationStatus, int deskId, int numberOfTickets, double reservationAmount, List<ReservationDesks> reservationDesks, Customer customer, Discount discount, Staff staff) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationStatus = reservationStatus;
        this.deskId = deskId;
        this.numberOfTickets = numberOfTickets;
        this.reservationAmount = reservationAmount;
        this.reservationDesks = reservationDesks;
        this.customer = customer;
        this.discount = discount;
        this.staff = staff;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(int reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public double getReservationAmount() {
        return reservationAmount;
    }

    public void setReservationAmount(double reservationAmount) {
        this.reservationAmount = reservationAmount;
    }

    public List<ReservationDesks> getReservationDesks() {
        return reservationDesks;
    }

    public void setReservationDesks(List<ReservationDesks> reservationDesks) {
        this.reservationDesks = reservationDesks;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
