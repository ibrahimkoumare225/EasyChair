package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    User update(User user);
    Optional<User> findOne(Long id);
    List<User> findAll();
    void delete(Long id);
    Optional<User> findByEmail(String email);
    List<User> findByConferenceId(Long conferenceId);
}