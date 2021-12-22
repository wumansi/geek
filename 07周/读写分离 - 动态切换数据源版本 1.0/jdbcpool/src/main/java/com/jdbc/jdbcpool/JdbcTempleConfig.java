package com.jdbc.jdbcpool;

import com.jdbc.jdbcpool.muldatasource.MultiDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTempleConfig{
    @Autowired
    MultiDataSource multiDataSource;

    @Bean("jdbcTemplate")
    public JdbcTemplate getJdbc() {
        return new JdbcTemplate(multiDataSource);
    }
}
