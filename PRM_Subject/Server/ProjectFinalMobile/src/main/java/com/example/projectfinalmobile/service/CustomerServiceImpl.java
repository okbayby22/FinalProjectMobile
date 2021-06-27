package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.entity.Customer;
import com.example.projectfinalmobile.entity.Staff;
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
    @Autowired
    private StaffRepository staffRepo;
    public int login(String email, String password){
        Staff staff;
        if(cusRepo.findByCustomerEmailAndCustomerPassword(email,convertMD5(password))!=null){
            return 9;
        }
        else if(staffRepo.findByStaffEmailAndStaffPassword(email,convertMD5(password))!=null){
            staff = staffRepo.findByStaffEmailAndStaffPassword(email,convertMD5(password));
            return staff.getStaffRole();
        }
        return 0;
    }

    public boolean checkDuplicateEmail(String email){
        if(cusRepo.findByCustomerEmail(email)!=null){
            return true;
        }
        return false;
    }

    public Customer getUserInfor(String email){
        return cusRepo.findByCustomerEmail(email);
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

    public Customer updateCusInfor(Customer customer){
        Customer updateInfor= cusRepo.findByCustomerEmail(customer.getCustomerEmail());
        updateInfor.setCustomerName(customer.getCustomerName());
        updateInfor.setCustomerPhone(customer.getCustomerPhone());
        updateInfor.setCustomerAddress(customer.getCustomerAddress());
        return cusRepo.save(updateInfor);
    }

    public boolean checkPassword(String password,String email){
        Customer customer = cusRepo.findByCustomerEmail(email);
        if(customer.getCustomerPassword().equals(convertMD5(password))){
            return false;
        }
        return true;
    }

    public boolean updateCusPassword(String password,String email){
        Customer updateInfor= cusRepo.findByCustomerEmail(email);
        updateInfor.setCustomerPassword(convertMD5(password));
        if(cusRepo.save(updateInfor) != null){
            return true;
        }
        return false ;
    }
    public String convertMD5(String text){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(text.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return myHash;
    }
}
