package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createDatabaseAndTable(String databaseName) {
        // Создаем новую базу данных
        jdbcTemplate.execute("CREATE DATABASE " + databaseName);

        // Создаем подключение к новой базе данных
        DataSource newDataSource = DataSourceBuilder.create()
                .url("jdbc:postgresql://212.193.30.42:5432/" + databaseName)
                .username("admin")
                .password("x#.Umr6s9V\\;74K")
                .driverClassName("org.postgresql.Driver")
                .build();

        // Используем новое подключение для создания таблицы
        JdbcTemplate newDbTemplate = new JdbcTemplate(newDataSource);
        newDbTemplate.execute("CREATE TABLE doctors (" +
                "id BIGSERIAL PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "specialization VARCHAR(255)" +
                ")");
    }
}
