package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getDatabaseNameById(Long id) {
        String sql = "SELECT database_name FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
    }

    public void updateDatabaseNameById(Long id, String newDatabaseName) {
        jdbcTemplate.update("UPDATE users SET database_name = ? WHERE id = ?", newDatabaseName, id);
    }


    public void deleteAdminById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<String> getUniqueDescriptions() {
        String sql = "SELECT DISTINCT description FROM users";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public User authenticate(String login, String password, String description) {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ? AND description = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{login, password, description},
                (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setLogin(rs.getString("login"));
                    user.setPassword(rs.getString("password"));
                    user.setAdminRules(rs.getString("admin_rules"));
                    user.setDescription(rs.getString("description"));
                    return user;
                });
    }

    public void saveAdmin(String login, String password, String description, String databaseName) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setAdminRules("admin");
        user.setDescription(description);
        user.setDatabaseName(databaseName);

        userRepository.save(user);
    }
}

