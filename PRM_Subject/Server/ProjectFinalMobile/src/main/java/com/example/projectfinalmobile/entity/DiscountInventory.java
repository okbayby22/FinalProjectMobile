package com.example.projectfinalmobile.entity;

import javax.persistence.*;

@Entity
@Table(name = "DiscountInventory")
public class DiscountInventory {

    @Id
    @Column(name = "DiscountInventoryId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "discountId")
    private Discount discounts;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customers;

    @Column(length = 12)
    private String discountCode;

    public DiscountInventory() {
    }

    public DiscountInventory(int id, Discount discounts, Customer customers, String discountCode) {
        this.id = id;
        this.discounts = discounts;
        this.customers = customers;
        this.discountCode = discountCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Discount getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Discount discounts) {
        this.discounts = discounts;
    }

    public Customer getCustomers() {
        return customers;
    }

    public void setCustomers(Customer customers) {
        this.customers = customers;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
}
