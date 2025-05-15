package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Role;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login"; // Returns src/main/resources/templates/login.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Returns src/main/resources/templates/register.html
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        // Check if email or pseudo already exists
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already exists.");
            return "register";
        }
        if (userService.findAll().stream().anyMatch(u -> u.getPseudo().equals(user.getPseudo()))) {
            model.addAttribute("error", "Pseudo already exists.");
            return "register";
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        /*// Set default role (AUTHOR)
        Role role = new Role();
        role.setRoleType(RoleType.AUTHOR);
        role.setUser(user);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);*/

        // Set birthDate (required field)
        user.setBirthDate(LocalDate.now()); // Adjust as needed (e.g., accept from form)

        // Save user
        userService.save(user);

        return "redirect:/login?registered=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}