package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.repository.UserRepository;
import fr.upsaclay.easychair.service.AuthorService;
import fr.upsaclay.easychair.service.OrganizerService;
import fr.upsaclay.easychair.service.ReviewerService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ReviewerService reviewerService;
    private final AuthorService authorService;
    private final OrganizerService organizerService;




    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOne(Long id) {
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
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User>findByConferenceId(Long conferenceId){
        List<Reviewer> reviewers=reviewerService.findByConferenceID(conferenceId);
        List<Author> authors= authorService.findByConferenceID(conferenceId);
        List<Organizer> organizers= organizerService.findByConferenceID(conferenceId);
        List<Role> allRoles = new ArrayList<>();
        allRoles.addAll(reviewers);
        allRoles.addAll(authors);
        allRoles.addAll(organizers);

        List<User> users = allRoles.stream()
                .map(Role::getUser)
                .distinct()  // enlève les doublons
                .toList();
        return users;


    }
}