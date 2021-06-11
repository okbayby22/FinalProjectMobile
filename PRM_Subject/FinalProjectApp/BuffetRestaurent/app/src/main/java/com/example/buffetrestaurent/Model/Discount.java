package com.example.buffetrestaurent.Model;

public class Discount {

    private int discountId;
    private int discountPercent;
    private int discountStatus;
    private int discountPoint;
    private String discountDescription;
    private int staffId;

    public Discount() {
    }

    public Discount(int discountId, int discountPercent, int discountStatus, int discountPoint, String discountDescription, int staffId) {
        this.discountId = discountId;
        this.discountPercent = discountPercent;
        this.discountStatus = discountStatus;
        this.discountPoint = discountPoint;
        this.discountDescription = discountDescription;
        this.staffId = staffId;
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

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
