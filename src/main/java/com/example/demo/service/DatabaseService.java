package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> fetchAllAdmins() {
        String sql = "SELECT login, password, description, database_name FROM users";
        return jdbcTemplate.queryForList(sql);
    }

    public void createDatabaseAndTable(String databaseName) {
        jdbcTemplate.execute("CREATE DATABASE " + databaseName);

        DataSource newDataSource = DataSourceBuilder.create()
                .url("jdbc:postgresql://212.193.30.42:5432/" + databaseName)
                .username("admin")
                .password("x#.Umr6s9V\\;74K")
                .driverClassName("org.postgresql.Driver")
                .build();

        JdbcTemplate newDbTemplate = new JdbcTemplate(newDataSource);
        newDbTemplate.execute("CREATE TABLE doctors (" +
                "id BIGSERIAL PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "specialization VARCHAR(255)" +
                ")");
    }
}
