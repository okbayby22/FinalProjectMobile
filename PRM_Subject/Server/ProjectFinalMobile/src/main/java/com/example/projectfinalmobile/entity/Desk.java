package com.example.projectfinalmobile.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Desk {

    @Id
    private int deskId;

    @Column(length = 10)
    private int seat;

    @Column(length = 1)
    private int deskStatus;

    @OneToMany(mappedBy = "desks", cascade = CascadeType.ALL)
    private Set<ReservationDesks> reservationDesks;
    public Desk() {
    }

    public Desk(int deskId, int seat, int deskStatus, Set<ReservationDesks> reservationDesks) {
        this.deskId = deskId;
        this.seat = seat;
        this.deskStatus = deskStatus;
        this.reservationDesks = reservationDesks;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getDeskStatus() {
        return deskStatus;
    }

    public void setDeskStatus(int deskStatus) {
        this.deskStatus = deskStatus;
    }

    public Set<ReservationDesks> getReservationDesks() {
        return reservationDesks;
    }

    public void setReservationDesks(Set<ReservationDesks> reservationDesks) {
        this.reservationDesks = reservationDesks;
    }
}
