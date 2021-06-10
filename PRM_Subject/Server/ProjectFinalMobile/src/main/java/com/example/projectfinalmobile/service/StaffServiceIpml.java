package com.example.projectfinalmobile.service;

import com.example.projectfinalmobile.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceIpml implements StaffService{

    @Autowired
    private StaffRepository staffRepo;

    public int login (String email, String password){
        if(staffRepo.findByStaffEmailAndStaffPassword(email,password)!=null){
            return staffRepo.findByStaffEmailAndStaffPassword(email,password).getStaffRole();
        }
        return 0;
    }
}
