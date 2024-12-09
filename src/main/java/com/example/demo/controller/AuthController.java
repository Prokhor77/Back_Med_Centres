package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/descriptions")
    public ResponseEntity<List<String>> getDescriptions() {
        List<String> descriptions = userService.getUniqueDescriptions();
        return ResponseEntity.ok(descriptions);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword(), loginRequest.getDescription());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid login, password, or description");
        }

        return ResponseEntity.ok(new LoginResponse(user.getLogin(), user.getAdminRules()));
    }

    // Вспомогательные классы
    static class LoginRequest {
        private String login;
        private String password;
        private String description;  // Добавляем описание в запрос

        // Getters and setters
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getDescription() { return description; }  // Геттер для description
        public void setDescription(String description) { this.description = description; }  // Сеттер для description
    }

    static class LoginResponse {
        private String login;
        private String role;

        public LoginResponse(String login, String role) {
            this.login = login;
            this.role = role;
        }

        // Getters
        public String getLogin() { return login; }
        public String getRole() { return role; }
    }
}
