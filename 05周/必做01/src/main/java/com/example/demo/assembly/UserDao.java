package com.example.demo.assembly;

import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    public void queryUser(){
        System.out.println("查询User");
    }
}
