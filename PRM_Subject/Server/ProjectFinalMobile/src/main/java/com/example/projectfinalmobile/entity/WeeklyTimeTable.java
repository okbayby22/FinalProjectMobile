package com.example.projectfinalmobile.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class WeeklyTimeTable {

    @Id
    private int staffId;
    private int shift;
    private Date workingDate;
    private int attendanceStatus;

    public WeeklyTimeTable() {
    }

    public WeeklyTimeTable(int staffId, int shift, Date workingDate, int attendanceStatus) {
        this.staffId = staffId;
        this.shift = shift;
        this.workingDate = workingDate;
        this.attendanceStatus = attendanceStatus;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
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
}
