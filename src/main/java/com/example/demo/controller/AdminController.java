package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.DatabaseService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private UserService userService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<Map<String, Object>>> fetchAllAdmins() {
        List<Map<String, Object>> admins = databaseService.fetchAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            String databaseName = userService.getDatabaseNameById(id);

            if (databaseName == null) {
                return ResponseEntity.badRequest().body("Администратор с таким ID не найден.");
            }

            userService.deleteAdminById(id);

            databaseService.dropDatabase(databaseName);

            return ResponseEntity.ok("Администратор и его база данных успешно удалены.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Ошибка при удалении администратора: " + ex.getMessage());
        }
    }

    @PutMapping("/updateDatabase")
    public ResponseEntity<?> updateDatabaseName(@RequestBody UpdateRequest updateRequest) {
        try {
            Long id = updateRequest.getId();
            String newDatabaseName = updateRequest.getDatabaseName();

            String oldDatabaseName = userService.getDatabaseNameById(id);
            if (oldDatabaseName == null) {
                return ResponseEntity.badRequest().body("Администратор с таким ID не найден.");
            }

            databaseService.renameDatabase(oldDatabaseName, newDatabaseName);

            userService.updateDatabaseNameById(id, newDatabaseName);

            return ResponseEntity.ok("Название базы данных успешно обновлено.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Ошибка при обновлении: " + ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody AdminRequest adminRequest) {
        if (adminRequest.getLogin() == null || adminRequest.getPassword() == null ||
                adminRequest.getDescription() == null || adminRequest.getDatabaseName() == null) {
            return ResponseEntity.badRequest().body("Все поля должны быть заполнены.");
        }
        databaseService.createDatabaseAndTable(adminRequest.getDatabaseName());

        userService.saveAdmin(adminRequest.getLogin(), adminRequest.getPassword(),
                adminRequest.getDescription(), adminRequest.getDatabaseName());
        return ResponseEntity.ok("Администратор создан успешно.");
    }


    static class UpdateRequest {
        private Long id;
        private String databaseName;

        // Getters и setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDatabaseName() { return databaseName; }
        public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
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