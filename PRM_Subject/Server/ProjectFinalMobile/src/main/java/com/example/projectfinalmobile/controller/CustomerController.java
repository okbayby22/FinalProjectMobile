package com.example.projectfinalmobile.controller;

import com.example.projectfinalmobile.entity.Customer;
import com.example.projectfinalmobile.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(path = "/customer/api")
public class CustomerController {

    @Autowired
    CustomerServiceImpl service;

    @GetMapping("/{email}/{password}/login")
    public int login(@PathVariable("email")String email, @PathVariable("password")String password){
        return service.login(email,password);
    }

    @GetMapping("/{email}/email")
    public boolean checkDuplicateEmail(@PathVariable("email") String email){
        return service.checkDuplicateEmail(email);
    }

    @GetMapping("/{phone}/phone")
    public boolean checkDuplicatePhone(@PathVariable("phone") String phone){
        return service.checkDuplicatePhone(phone);
    }

    @PostMapping("/insert")
    public Customer insertCustomer(@RequestBody Customer customer){
        return service.insertCustomer(customer);
    }
}
