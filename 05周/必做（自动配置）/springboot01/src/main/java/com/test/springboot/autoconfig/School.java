package com.test.springboot.autoconfig;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class School{
    private String name ="希望小学";
    public void ding() {
        System.out.println("school name" + this.name);
    }
}
