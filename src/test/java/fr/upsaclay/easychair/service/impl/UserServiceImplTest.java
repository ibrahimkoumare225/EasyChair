package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    private User user2;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test


        user = new User();
        user.setId(1L);


        user2 = new User();
        user2.setId(2L);


        userList = Arrays.asList(user, user2);

    }

}