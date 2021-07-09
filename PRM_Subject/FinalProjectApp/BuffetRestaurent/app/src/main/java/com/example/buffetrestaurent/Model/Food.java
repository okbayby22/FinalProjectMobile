package com.example.buffetrestaurent.Model;


public class Food {

    private int foodId;
    private String foodName;
    private String foodDescription;
    private String foodImage;
    private int foodType;

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }

    public int getFoodType() {
        return foodType;
    }

    public Food() {
    }

    public Food(int foodId, String foodName, String foodDescription, String foodImage) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
