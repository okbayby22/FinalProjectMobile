package com.example.buffetrestaurent.Model;


public class Desk {

    private int desk_id;
    private int seat;
    private int desk_status;

    public Desk() {
    }

    public Desk(int deskId, int seat, int deskStatus) {
        this.desk_id = deskId;
        this.seat = seat;
        this.desk_status = deskStatus;
    }

    public int getDesk_id() {
        return desk_id;
    }

    public void setDesk_id(int desk_id) {
        this.desk_id = desk_id;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getDesk_status() {
        return desk_status;
    }

    public void setDesk_status(int desk_status) {
        this.desk_status = desk_status;
    }
}
