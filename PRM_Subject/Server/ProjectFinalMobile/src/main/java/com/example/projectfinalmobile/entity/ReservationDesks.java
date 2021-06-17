package com.example.projectfinalmobile.entity;

import javax.persistence.*;

@Entity
@Table(name = "ReservationDesks")
public class ReservationDesks {

    @Id
    @Column(name = "ReservationDesksId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "deskId")
    private Desk desks;

    @ManyToOne
    @JoinColumn(name = "reservationId")
    private Reservation reservations;

    public ReservationDesks() {
    }

    public ReservationDesks(int id, Desk desks, Reservation reservations) {
        this.id = id;
        this.desks = desks;
        this.reservations = reservations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Desk getDesks() {
        return desks;
    }

    public void setDesks(Desk desks) {
        this.desks = desks;
    }

    public Reservation getReservations() {
        return reservations;
    }

    public void setReservations(Reservation reservations) {
        this.reservations = reservations;
    }
}
