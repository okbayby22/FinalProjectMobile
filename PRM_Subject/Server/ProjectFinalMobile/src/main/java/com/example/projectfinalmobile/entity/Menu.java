package com.example.projectfinalmobile.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Menu {

    @Id
    private int menuId;

    private Date menuDate;

    @Column(length = 200)
    private String menuName;

    @OneToMany(mappedBy = "menu")
    private Set<MenuDetail> menuDetails;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private Staff staff;

    public Menu() {
    }

    public Menu(int menuId, Date menuDate, String menuName, Set<MenuDetail> menuDetails, Staff staff) {
        this.menuId = menuId;
        this.menuDate = menuDate;
        this.menuName = menuName;
        this.menuDetails = menuDetails;
        this.staff = staff;
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

    public Set<MenuDetail> getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(Set<MenuDetail> menuDetails) {
        this.menuDetails = menuDetails;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
