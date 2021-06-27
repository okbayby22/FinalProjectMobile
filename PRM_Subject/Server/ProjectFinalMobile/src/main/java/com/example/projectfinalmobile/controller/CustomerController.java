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

    @GetMapping("/{email}/userinfor")
    public Customer getUserInfor(@PathVariable("email") String email){
        return service.getUserInfor(email);
    }

    @GetMapping("/{phone}/phone")
    public boolean checkDuplicatePhone(@PathVariable("phone") String phone){
        return service.checkDuplicatePhone(phone);
    }

    @PostMapping("/insert")
    public Customer insertCustomer(@RequestBody Customer customer){
        return service.insertCustomer(customer);
    }

    @PutMapping("/updateInfor")
    public Customer updateCusInfor(@RequestBody Customer customer){
        return service.updateCusInfor(customer);
    }

    @PutMapping("/{password}/{email}/updateCusPassword")
    public boolean updateCusPassword(@PathVariable("password")String password,@PathVariable("email")String email){
        return service.updateCusPassword(password,email);
    }
    @GetMapping("/{password}/{email}/checkPassword")
    public boolean checkPassword(@PathVariable("password")String password,@PathVariable("email")String email){
        return service.checkPassword(password,email);
    }
}
