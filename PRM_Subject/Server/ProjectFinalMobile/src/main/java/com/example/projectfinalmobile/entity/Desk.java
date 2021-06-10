package com.example.projectfinalmobile.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Desk {

    @Id
    private int deskId;
    private int seat;
    private int deskStatus;

    public Desk() {
    }

    public Desk(int deskId, int seat, int deskStatus) {
        this.deskId = deskId;
        this.seat = seat;
        this.deskStatus = deskStatus;
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
}
