package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.entity.Customer;
import com.example.projectfinalmobile.repository.CustomerRepository;
import com.example.projectfinalmobile.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerServices {

    @Autowired
    private CustomerRepository cusRepo;

    public int login(String email, String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        if(cusRepo.findByCustomerEmailAndCustomerPassword(email,myHash)!=null){
            return 9;
        }
        return 0;
    }

    public boolean checkDuplicateEmail(String email){
        if(cusRepo.findByCustomerEmail(email)!=null){
            return true;
        }
        return false;
    }

    public boolean checkDuplicatePhone(String phone){
        if(cusRepo.findByCustomerPhone(phone)!=null){
            return true;
        }
        return false;
    }

    public Customer insertCustomer(Customer customer){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(customer.getCustomerPassword().getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        customer.setCustomerPassword(myHash);
        return cusRepo.save(customer);
    }
}
