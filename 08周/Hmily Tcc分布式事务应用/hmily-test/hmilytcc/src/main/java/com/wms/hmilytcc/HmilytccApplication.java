package com.wms.hmilytcc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;

@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.wms.hmilytcc.mapper")
@ImportResource({"classpath:applicationContext.xml"})
@SpringBootApplication
public class HmilytccApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmilytccApplication.class, args);
	}

}
