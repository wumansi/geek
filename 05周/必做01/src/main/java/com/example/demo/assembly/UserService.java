package com.example.demo.assembly;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class UserService {
    private String serviceName;
    public UserService(){
        this.serviceName = "sisi";
    }
    public void print(){
        System.out.println("property:" + this.serviceName);
    }
}
