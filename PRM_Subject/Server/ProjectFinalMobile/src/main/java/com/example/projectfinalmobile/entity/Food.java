package com.example.projectfinalmobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Food {

    @Id
    private int foodId;

    @Column(length = 100)
    private String foodName;

    @Column(length = 255)
    private String foodDescription;

    @Column(length = 255)
    private String foodImage;

    @OneToMany(mappedBy = "food")
    private Set<MenuDetail> menuDetails;

    public Food() {
    }

    public Food(int foodId, String foodName, String foodDescription, String foodImage, Set<MenuDetail> menuDetails) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
        this.menuDetails = menuDetails;
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

    public Set<MenuDetail> getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(Set<MenuDetail> menuDetails) {
        this.menuDetails = menuDetails;
    }
}
