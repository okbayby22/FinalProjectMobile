package com.example.projectfinalmobile.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(length = 100)
    private String customerName;

    @Column(nullable = false,length = 255)
    private String customerEmail;

    @Column(length = 255)
    private String customerAddress;

    @Column(length = 13)
    private String customerPhone;

    @Column(nullable = false, length = 32)
    private String customerPassword;

    @Column(length = 1)
    private int customerGender;

    @Column(length = 15)
    private double customerBalance;

    @Column(length = 10)
    private int customerPoint;

    @Column(length = 1)
    private int customerStatus;

    @Column(length = 255)
    private String customerAvatar;

    @OneToMany(mappedBy = "customers")
    private Set<DiscountInventory> discountInventory;

    @OneToMany(mappedBy = "customer")
    private Set<Reservation> reservations;

    public Customer() {
    }

    public Customer(int customerId, String customerName, String customerEmail, String customerAddress, String customerPhone, String customerPassword, int customerGender, double customerBalance, int customerPoint, int customerStatus, String customerAvatar, Set<DiscountInventory> discountInventory, Set<Reservation> reservations) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerPassword = customerPassword;
        this.customerGender = customerGender;
        this.customerBalance = customerBalance;
        this.customerPoint = customerPoint;
        this.customerStatus = customerStatus;
        this.customerAvatar = customerAvatar;
        this.discountInventory = discountInventory;
        this.reservations = reservations;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public int getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(int customerGender) {
        this.customerGender = customerGender;
    }

    public double getCustomerBalance() {
        return customerBalance;
    }

    public void setCustomerBalance(double customerBalance) {
        this.customerBalance = customerBalance;
    }

    public int getCustomerPoint() {
        return customerPoint;
    }

    public void setCustomerPoint(int customerPoint) {
        this.customerPoint = customerPoint;
    }

    public int getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(int customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerAvatar() {
        return customerAvatar;
    }

    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
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
}
