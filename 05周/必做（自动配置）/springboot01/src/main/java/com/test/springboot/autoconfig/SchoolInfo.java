package com.test.springboot.autoconfig;

import lombok.Data;

@Data
public class SchoolInfo {

    public SchoolInfo(String name){
        this.name = name;
    }

    private String name;
}
