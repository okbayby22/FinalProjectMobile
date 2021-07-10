package com.example.buffetrestaurent.Model;

public class Staff {

    private String staffId;
    private String staffName;
    private String staffEmail;
    private String staffAddress;
    private String staffPhone;
    private String staffPassword;
    private int staffGender;
    private int staffRole;
    private int staffStatus;
    private String staffImage;

    public Staff() {
    }

    public Staff(String staffId, String staffName, String staffEmail, String staffAddress, String staffPhone, String staffPassword, int staffGender, int staffRole, int staffStatus, String staffImage) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffEmail = staffEmail;
        this.staffAddress = staffAddress;
        this.staffPhone = staffPhone;
        this.staffPassword = staffPassword;
        this.staffGender = staffGender;
        this.staffRole = staffRole;
        this.staffStatus = staffStatus;
        this.staffImage = staffImage;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffAddress() {
        return staffAddress;
    }

    public void setStaffAddress(String staffAddress) {
        this.staffAddress = staffAddress;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    public int getStaffGender() {
        return staffGender;
    }

    public void setStaffGender(int staffGender) {
        this.staffGender = staffGender;
    }

    public int getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(int staffRole) {
        this.staffRole = staffRole;
    }

    public int getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(int staffStatus) {
        this.staffStatus = staffStatus;
    }

    public String getStaffImage() {
        return staffImage;
    }

    public void setStaffImage(String staffImage) {
        this.staffImage = staffImage;
    }
}
