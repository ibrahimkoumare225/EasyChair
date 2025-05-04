package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
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
        Optional<User> user = userService.findOne(id);
        return user.isPresent() ? (ResponseEntity<User>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    // PUT /users/{id}
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    // DELETE /users/{id}
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    //GET /users/search
    @GetMapping("/search")
    public List<User> getByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrPseudoIgnoreCaseOrEmailIgnoreCase(@RequestBody String firstName, @RequestBody String lastName,@RequestBody String pseudo,@RequestBody String email) {
        return userService.findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrPseudoIgnoreCaseOrEmailIgnoreCase(firstName,lastName,pseudo,email);
    }
}

