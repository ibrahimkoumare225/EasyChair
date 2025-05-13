package fr.upsaclay.easychair.service;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.repository.UserRepository;
import fr.upsaclay.easychair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Component
public class DataInit implements CommandLineRunner, Ordered {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running DataInitializer...");
        initializeData();
        runSqlScript("classpath:data.sql");
    }

    @Transactional
    public void initializeData() {
        // Creation de user1
        if (userRepository.findByEmail("john@doe.com").isEmpty()) {
            User user1 = new User();
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setEmail("john@doe.com");
            user1.setPseudo("toto");
            user1.setPassword(passwordEncoder.encode("password123")); // Valid password (>= 6 characters)
            user1.setBirthDate(LocalDate.of(2000, 1, 1));
            user1 = userService.save(user1);
        }

        // Creation de user2
        if (userRepository.findByEmail("jane@doe.com").isEmpty()) {
            User user2 = new User();
            user2.setFirstName("Jane");
            user2.setLastName("Doe");
            user2.setPseudo("tata");
            user2.setEmail("jane@doe.com");
            user2.setPassword(passwordEncoder.encode("password123")); // Valid password (>= 6 characters)
            user2.setBirthDate(LocalDate.of(2001, 1, 1));
            user2 = userService.save(user2);
        }

        // Creation de user3
        if (userRepository.findByEmail("alban@cousin.com").isEmpty()) {
            User user3 = new User();
            user3.setFirstName("Alban");
            user3.setLastName("Cousin");
            user3.setEmail("alban@cousin.com");
            user3.setPseudo("Banban");
            user3.setPassword(passwordEncoder.encode("alban123")); // Valid password (>= 6 characters)
            user3.setBirthDate(LocalDate.of(2003, 4, 12));
            user3 = userService.save(user3);
        }

        // Creation de user4
        if (userRepository.findByEmail("jeremie@pennec.com").isEmpty()) {
            User user4 = new User();
            user4.setFirstName("Jeremie");
            user4.setLastName("Pennec");
            user4.setEmail("jeremie@pennec.com");
            user4.setPseudo("Jeje");
            user4.setPassword(passwordEncoder.encode("jeremie123")); // Valid password (>= 6 characters)
            user4.setBirthDate(LocalDate.of(1980, 12, 12));
            user4 = userService.save(user4);
        }

        // Creation de user5
        if (userRepository.findByEmail("ibrahim@koumare.com").isEmpty()) {
            User user5 = new User();
            user5.setFirstName("Ibrahim");
            user5.setLastName("Koumare");
            user5.setEmail("ibrahim@koumare.com");
            user5.setPseudo("ibrah");
            user5.setPassword(passwordEncoder.encode("ibrahim123"));// Valid password (>= 6 characters)
            user5.setBirthDate(LocalDate.of(2000, 12, 12));
            user5 = userService.save(user5);
        }
    }



    private void runSqlScript(String scriptPath) throws IOException {
        Resource resource = resourceLoader.getResource(scriptPath);
        String sql = Files.readString(Path.of(resource.getURI()));
        jdbcTemplate.execute(sql);
    }

    @Override
    public int getOrder() {
        return -1; // PrioritÃ© haute
    }
}
