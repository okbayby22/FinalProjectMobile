package com.example.demomysql.ModelDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DAO {

    @Autowired
    JdbcTemplate template;

    public List<Map<String, Object>> listStu() {
        List<Map<String, Object>> list = template.queryForList("select * from student");
        return list;
    }
}
