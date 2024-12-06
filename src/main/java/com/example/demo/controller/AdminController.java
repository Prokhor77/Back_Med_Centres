package com.example.demo.controller;

import com.example.demo.service.DatabaseService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody AdminRequest adminRequest) {
        // Проверяем, что все данные заполнены
        if (adminRequest.getLogin() == null || adminRequest.getPassword() == null ||
                adminRequest.getDescription() == null || adminRequest.getDatabaseName() == null) {
            return ResponseEntity.badRequest().body("Все поля должны быть заполнены.");
        }

        // Создаем базу данных и таблицу
        databaseService.createDatabaseAndTable(adminRequest.getDatabaseName());

        // Сохраняем пользователя в основной базе данных
        userService.saveAdmin(adminRequest.getLogin(), adminRequest.getPassword(),
                adminRequest.getDescription(), adminRequest.getDatabaseName());

        return ResponseEntity.ok("Администратор создан успешно.");
    }

    static class AdminRequest {
        private String login;
        private String password;
        private String description;
        private String databaseName;

        // Getters и setters
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDatabaseName() { return databaseName; }
        public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
    }
}