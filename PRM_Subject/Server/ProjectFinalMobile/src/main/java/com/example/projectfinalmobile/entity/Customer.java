package com.example.projectfinalmobile.entity;

import javax.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String customerPhone;
    private String customerPassword;
    private int customerGender;
    private double customerBalance;
    private int customerPoint;
    private int customerStatus;
    private String customerAvatar;

    public Customer() {
    }

    public Customer(int customerId, String customerName, String customerEmail, String customerAddress, String customerPhone, String customerPassword, int customerGender, double customerBalance, int customerPoint, int customerStatus, String customerAvatar) {
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
}
