package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid login or password");
        }

        return ResponseEntity.ok(new LoginResponse(user.getLogin(), user.getAdminRules()));
    }

    // Вспомогательные классы
    static class LoginRequest {
        private String login;
        private String password;

        // Getters and setters
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
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