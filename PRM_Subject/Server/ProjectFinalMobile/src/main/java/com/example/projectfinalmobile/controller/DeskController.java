package com.example.projectfinalmobile.controller;

import com.example.projectfinalmobile.entity.Desk;
import com.example.projectfinalmobile.service.DeskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path ="/desk/api")
public class DeskController {

    @Autowired
    DeskServiceImpl service;

    @GetMapping("/deskList")
    public List<Desk> loaddeskList(){
        return service.loaddeskList();
    }
}
