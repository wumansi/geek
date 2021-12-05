package com.test.springboot.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "school")
public class SchoolProperties {
    private String schoolName = "sisi";

    public String getSchoolName(){
        return schoolName;
    }
    public void setSchoolName(String schoolName){
        this.schoolName=schoolName;
    }
}
