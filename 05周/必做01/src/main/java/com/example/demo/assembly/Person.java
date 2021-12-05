package com.example.demo.assembly;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Person {
    private String name;
    private int num;

    public void say(){
        System.out.println("person say" + this.name + this.name);
    }
}
