package com.example.projectfinalmobile.repository;

import com.example.projectfinalmobile.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskRepository extends JpaRepository<Desk,Integer> {

}
