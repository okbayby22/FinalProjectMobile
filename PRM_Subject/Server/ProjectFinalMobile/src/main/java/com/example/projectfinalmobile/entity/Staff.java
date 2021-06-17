package com.example.projectfinalmobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Staff {

    @Id
    private int staffId;

    @Column(length = 100)
    private String staffName;

    @Column(length = 255)
    private String staffEmail;

    @Column(length = 255)
    private String staffAddress;

    @Column(length = 13)
    private String staffPhone;

    @Column(length = 32)
    private String staffPassword;

    @Column(length = 1)
    private int staffGender;

    @Column(length = 1)
    private int staffRole;

    @Column(length = 1)
    private int staffStatus;

    @Column(length = 255)
    private String staffImage;

    @OneToMany(mappedBy = "staff")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "staff")
    private Set<Discount> discounts;

    @OneToMany(mappedBy = "staff")
    private Set<WeeklyTimeTable> weeklyTimeTables;

    @OneToMany(mappedBy = "staff")
    private Set<Menu> menus;

    public Staff() {
    }

    public Staff(int staffId, String staffName, String staffEmail, String staffAddress, String staffPhone, String staffPassword, int staffGender, int staffRole, int staffStatus, String staffImage, Set<Reservation> reservations, Set<Discount> discounts, Set<WeeklyTimeTable> weeklyTimeTables, Set<Menu> menus) {
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
        this.reservations = reservations;
        this.discounts = discounts;
        this.weeklyTimeTables = weeklyTimeTables;
        this.menus = menus;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
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

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public Set<WeeklyTimeTable> getWeeklyTimeTables() {
        return weeklyTimeTables;
    }

    public void setWeeklyTimeTables(Set<WeeklyTimeTable> weeklyTimeTables) {
        this.weeklyTimeTables = weeklyTimeTables;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }
}
