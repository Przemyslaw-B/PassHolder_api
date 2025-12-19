package com.program.passholder.Database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @Bean
    public DBConnection dbConnection(DataSource dataSource) {
        return new DBConnection(dataSource);
    }
}
