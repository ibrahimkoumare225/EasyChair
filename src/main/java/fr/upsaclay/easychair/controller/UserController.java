package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserByID(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // POST /users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    // PUT /users
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    // DELETE /users/{id}
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}

