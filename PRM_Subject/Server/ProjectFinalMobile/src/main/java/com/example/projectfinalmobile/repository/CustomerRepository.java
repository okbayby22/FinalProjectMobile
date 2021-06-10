package com.example.projectfinalmobile.repository;

import com.example.projectfinalmobile.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer> {
    Customer findByCustomerEmailAndCustomerPassword( String email,String password);
    Customer findByCustomerEmail(String email);
    Customer findByCustomerPhone(String phone);
}
