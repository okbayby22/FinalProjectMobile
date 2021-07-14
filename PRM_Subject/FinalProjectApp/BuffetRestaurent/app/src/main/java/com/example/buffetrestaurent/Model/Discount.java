package com.example.buffetrestaurent.Model;

public class Discount {

    private String discountId;
    private int discountPercent;
    private int discountStatus;
    private int discountPoint;
    private String discountName;
    private String staffId;

    public Discount() {
    }

    public Discount(String discountId, int discountPercent, int discountStatus, int discountPoint, String discountName, String staffId) {
        this.discountId = discountId;
        this.discountPercent = discountPercent;
        this.discountStatus = discountStatus;
        this.discountPoint = discountPoint;
        this.discountName = discountName;
        this.staffId = staffId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
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

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountDescription) {
        this.discountName = discountDescription;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
