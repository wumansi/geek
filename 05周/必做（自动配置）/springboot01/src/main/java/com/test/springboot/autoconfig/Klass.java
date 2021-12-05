package com.test.springboot.autoconfig;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class Klass {
    private String name = "三年二班";
    public void dong(){
        System.out.println(this.getName());
    }
}
