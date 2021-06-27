package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.entity.Desk;
import com.example.projectfinalmobile.repository.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeskServiceImpl implements DeskService{
    @Autowired
    DeskRepository deskRepository;

    public List<Desk> loaddeskList(){
        return deskRepository.findAll();
    };

}
