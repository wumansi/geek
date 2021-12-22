package com.jdbc.jdbcpool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
public class HikariConfig {

//    @Bean
//    @ConfigurationProperties("spring.datasource")
//    public DataSource dataSource(){
//        return DataSourceBuilder.create().build();
//    }
}
