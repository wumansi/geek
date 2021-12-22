package com.jdbc.jdbcpool;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
//
////    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    @Bean("db1DataSource")
    public DataSource druid(){
        return DruidDataSourceBuilder.create().build();
    }
//
//    @Primary
//    @Bean("hikariDataSource")
//    @ConfigurationProperties("spring.datasource.db2")
//    public DataSource dataSource(){
//        return DataSourceBuilder.create().build();
//    }

    @ConfigurationProperties(prefix = "spring.datasource.db2")
    @Bean("db2DataSource")
    public DataSource dataSource2(){
        return DruidDataSourceBuilder.create().build();
    }

//    @Primary
//    @Bean("dynamicDS")
//    public DataSource dataSource(){
//        MultiDataSource dds = new MultiDataSource();
//        dds.setDefaultTargetDataSource(druid());
//        Map<Object,Object> dsMap = new HashMap<>();
//        dsMap.put("dataSource", druid());
//        dsMap.put("dataSource2", dataSource2());
//        dds.setTargetDataSources(dsMap);
//        return dds;
//    }

}
