package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Notification;
import fr.upsaclay.easychair.model.Role;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.repository.UserRepository;
import fr.upsaclay.easychair.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        // Vérification que l'utilisateur et son ID ne sont pas null
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or user ID cannot be null");
        }
        // Utilisation de findOne pour récupérer l'utilisateur existant
        return findOne(user.getId())
                .map(existingUser -> {
                    // Mise à jour des champs
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setPseudo(user.getPseudo());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setBirthDate(user.getBirthDate());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setRoles(user.getRoles());
                    // Sauvegarde et retour de l'utilisateur mis à jour
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l’ID : " + user.getId()));
    }
    @Override
    public Optional<User> findOne(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrPseudoIgnoreCaseOrEmailIgnoreCase(String firstName, String lastName, String pseudo, String email) {
        if (firstName == null || lastName == null || pseudo == null || email == null) {
            throw new IllegalArgumentException("Au moins un des champs est requis");
        }
        return userRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrPseudoIgnoreCaseOrEmailIgnoreCase(firstName, lastName, pseudo, email);
    }
}
