package com.test.springboot;

import com.test.springboot.autoconfig.SchoolInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Springboot01ApplicationTests {

    @Autowired
    SchoolInfo info;

    @Test
    public void testauto() {
        System.out.println(info.getName());
    }

}