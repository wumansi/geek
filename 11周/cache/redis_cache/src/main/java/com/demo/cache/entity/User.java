package com.demo.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private Integer id;
    private String  username;
    private String password;
    public User(Integer id, String username){
        this.id = id;
        this.username = username;
    }
}
