package com.example.demomysql.controler;

import java.util.List;
import java.util.Map;

import com.example.demomysql.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentControler {
    @Autowired
    private StudentService service;

    @GetMapping("/listar")
    public List<Map<String, Object>> listar(Model model) {
        return service.listar();
    }
}
