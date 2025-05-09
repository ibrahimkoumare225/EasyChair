package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Notification;
import fr.upsaclay.easychair.model.Role;
import fr.upsaclay.easychair.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);


    User  update(User  user);

    Optional<User> findOne(Long id);

    List<User> findAll();

    void delete(Long id);

    List<User> findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrPseudoIgnoreCaseOrEmailIgnoreCase(String firstName,String lastName,String pseudo,String email);

}
