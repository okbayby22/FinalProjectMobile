package com.example.buffetrestaurent.Model;

import java.util.Date;

public class Menu {

    private String menuId;
    private String menuDate;
    private String staffId;

    public Menu() {
    }

    public Menu(String menuId, String menuDate, String staffId) {
        this.menuId = menuId;
        this.menuDate = menuDate;
        this.staffId = staffId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(String menuDate) {
        this.menuDate = menuDate;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
