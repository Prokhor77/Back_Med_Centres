package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User authenticate(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
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

