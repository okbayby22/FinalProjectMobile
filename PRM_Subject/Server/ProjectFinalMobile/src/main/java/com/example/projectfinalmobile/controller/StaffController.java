package com.example.projectfinalmobile.controller;

import com.example.projectfinalmobile.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/staff/api")
public class StaffController {

    @Autowired
    private StaffService service;

    @GetMapping("/{email}/{password}/login")
    public int login(@PathVariable("email")String email, @PathVariable("password")String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return service.login(email,myHash);
    }
}
