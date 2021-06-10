package com.example.projectfinalmobile.repository;

import com.example.projectfinalmobile.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff,Integer> {
    public Staff findByStaffEmailAndStaffPassword(String email,String passowrd);
}
