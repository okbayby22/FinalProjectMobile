package com.example.buffetrestaurent.Model;

public class DiscountInventory {
    private String customerId;
    private String discountId;

    public DiscountInventory() {
    }

    public DiscountInventory(String customerId, String discountId) {
        this.customerId = customerId;
        this.discountId = discountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }
}
