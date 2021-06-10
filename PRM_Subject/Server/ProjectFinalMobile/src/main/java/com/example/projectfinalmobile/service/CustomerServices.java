package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerServices {
    int login(String email, String password);
    boolean checkDuplicateEmail(String email);
    boolean checkDuplicatePhone(String phone);
    Customer insertCustomer(Customer customer);
}
