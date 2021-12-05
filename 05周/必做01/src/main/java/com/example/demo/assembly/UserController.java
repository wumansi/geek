package com.example.demo.assembly;

import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    public UserController(){
        System.out.println("初始化UserController");
    }

    public void print(){
        System.out.println("======>UserController");
    }
}
