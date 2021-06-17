package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface StaffService {
    int login (String email, String password);
}
