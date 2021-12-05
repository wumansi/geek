package com.example.demo.assembly;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private int id;
    private String name;

    public void print(){
        System.out.println(this.toString());
    }
}
