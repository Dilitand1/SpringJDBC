package ru.litvinov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("ru.litvinov")
public class Conf {

    @Bean
    public DataSource dataSource(){

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl("jdbc:postgresql://127.0.0.1:5434/javapooldb");
        driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
        driverManagerDataSource.setUsername("postgres");
        driverManagerDataSource.setPassword("password");
        return driverManagerDataSource;
    }

    @Bean
    public JdbcTemplate template (DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
