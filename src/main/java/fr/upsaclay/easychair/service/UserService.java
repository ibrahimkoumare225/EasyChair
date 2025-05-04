package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);


    User  update(User  user);

    User getUserByID(Long id);

    Optional<User> findOne(Long id);

    List<User> findAll();

    void delete(Long id);

    List<User> findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrPseudoIgnoreCaseOrEmailIgnoreCase(String firstName,String lastName,String pseudo,String email);

}
