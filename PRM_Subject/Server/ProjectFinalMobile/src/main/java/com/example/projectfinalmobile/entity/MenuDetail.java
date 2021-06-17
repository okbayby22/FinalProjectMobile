package com.example.projectfinalmobile.entity;

import javax.persistence.*;

@Entity
@Table(name = "MenuDetail")
public class MenuDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MenuDetailId")
    private int id;

    @ManyToOne
    @JoinColumn(name = "menuId")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "foodId")
    private Food food;
}
