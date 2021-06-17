package com.example.projectfinalmobile.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Discount {

    @Id
    private int discountId;

    @Column(length = 10)
    private int discountPercent;

    @Column(length = 1)
    private int discountStatus;

    @Column(length = 10)
    private int discountPoint;

    @Column(length = 255)
    private String discountDescription;

    @OneToMany(mappedBy = "discounts")
    private Set<DiscountInventory> discountInventory;

    @OneToMany(mappedBy = "discount")
    private Set<Reservation> reservations;

    @ManyToOne()
    @JoinColumn(name = "staffId")
    private Staff staff;

    public Discount() {
    }

    public Discount(int discountId, int discountPercent, int discountStatus, int discountPoint, String discountDescription, Set<DiscountInventory> discountInventory, Set<Reservation> reservations, Staff staff) {
        this.discountId = discountId;
        this.discountPercent = discountPercent;
        this.discountStatus = discountStatus;
        this.discountPoint = discountPoint;
        this.discountDescription = discountDescription;
        this.discountInventory = discountInventory;
        this.reservations = reservations;
        this.staff = staff;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getDiscountStatus() {
        return discountStatus;
    }

    public void setDiscountStatus(int discountStatus) {
        this.discountStatus = discountStatus;
    }

    public int getDiscountPoint() {
        return discountPoint;
    }

    public void setDiscountPoint(int discountPoint) {
        this.discountPoint = discountPoint;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    public void setDiscountDescription(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public Set<DiscountInventory> getDiscountInventory() {
        return discountInventory;
    }

    public void setDiscountInventory(Set<DiscountInventory> discountInventory) {
        this.discountInventory = discountInventory;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
