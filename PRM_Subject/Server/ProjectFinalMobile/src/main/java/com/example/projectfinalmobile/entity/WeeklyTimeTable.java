package com.example.projectfinalmobile.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WeeklyTimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 1)
    private int shift;

    private Date workingDate;

    @Column(length = 1)
    private int attendanceStatus;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private Staff staff;

    public WeeklyTimeTable() {
    }

    public WeeklyTimeTable(int id, int shift, Date workingDate, int attendanceStatus, Staff staff) {
        this.id = id;
        this.shift = shift;
        this.workingDate = workingDate;
        this.attendanceStatus = attendanceStatus;
        this.staff = staff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public Date getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate) {
        this.workingDate = workingDate;
    }

    public int getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(int attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
