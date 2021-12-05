package com.example.demo;

import com.example.demo.jdbc.JDBCController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTest {
    @Resource
    DataSource dataSource;

    @Autowired
    JDBCController controller;

    @Test
    public void contextLoads() {
        System.out.println("数据源" + dataSource.getClass().getName());
    }

    @Test
    public void testJdbc(){
        controller.queryStudent();
    }
}
