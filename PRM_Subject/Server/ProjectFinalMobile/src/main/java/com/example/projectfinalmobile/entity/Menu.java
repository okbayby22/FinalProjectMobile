package com.example.projectfinalmobile.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Menu {

    @Id
    private int menuId;
    private Date menuDate;
    private String menuName;
    private int staffId;

    public Menu() {
    }

    public Menu(int menuId, Date menuDate, String menuName, int staffId) {
        this.menuId = menuId;
        this.menuDate = menuDate;
        this.menuName = menuName;
        this.staffId = staffId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public Date getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(Date menuDate) {
        this.menuDate = menuDate;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
