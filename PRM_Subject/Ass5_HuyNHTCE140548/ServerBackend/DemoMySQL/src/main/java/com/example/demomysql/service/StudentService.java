package com.example.demomysql.service;

import com.example.demomysql.ModelDAO.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    @Autowired
    DAO dao;

    public List<Map<String, Object>> listar() {
        return dao.listStu();
    }
}
