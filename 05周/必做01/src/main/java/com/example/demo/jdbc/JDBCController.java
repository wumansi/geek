package com.example.demo.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class JDBCController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void queryStudent(){
        String sql = "select * from student";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map map : list){
            System.out.println(map.get("username"));
        }
    }
}
