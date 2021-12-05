package com.test.springboot.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({School.class, Klass.class, Student.class})
@EnableConfigurationProperties(SchoolProperties.class)
public class SchoolAutoConfiguration {
    @Autowired
    SchoolProperties properties;

    @Autowired
    School school;

    @Autowired
    Klass class1;

    @Autowired
    Student student;

    @Bean
    public SchoolInfo creatSchoolInfo(){
        return new SchoolInfo(school.getName() +
                class1.getName() + student.getName() + properties.getSchoolName());
    }
}
