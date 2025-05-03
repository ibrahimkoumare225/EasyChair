package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.model.enumates.Phase;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class DataInitializer {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private ReviewerRepository reviewerRepository;


    public void initializeData() {

        //Creation de user1
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john@doe.com");
        user1.setPseudo("toto");
        user1.setPassword("password");
        user1.setBirthDate(LocalDateTime.of(2000, 1, 1, 0, 0));
        user1=userRepository.save(user1);

        //Creation de user2
        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setPseudo("tata");
        user2.setEmail("jane@doe.com");
        user2.setPassword("password");
        user2.setBirthDate(LocalDateTime.of(2001, 1, 1, 0, 0));
        user2=userRepository.save(user2);

        // Création de la conférence
        Conference conference = new Conference();
        conference.setTitle("Sample Conference");
        conference.setDescription("This is a sample conference description.");
        conference.setCreationDate(LocalDate.now());
        conference.setCommiteeAssignmentDate(conference.getCreationDate().plusDays(10));
        conference.setAbstractSubDate(conference.getCommiteeAssignmentDate().plusDays(10));
        conference.setSubAssignmentDate(conference.getAbstractSubDate().plusDays(10));
        conference.setConcreteSubDate(conference.getSubAssignmentDate().plusDays(10));
        conference.setEvaluationDate(conference.getConcreteSubDate().plusDays(10));
        conference.setFinalSubDate(conference.getEvaluationDate().plusDays(10));
        conference.setEndDate(conference.getFinalSubDate().plusDays(10));
        conference.setPhase(Phase.INITIALIZATION);
        conference = conferenceRepository.save(conference);
        conferenceRepository.flush();
        /*
        conference.setOnInvitation(false);
        conference.setHiddenDescription(false);
        conference.setHiddenConf(false);
        conference.setAnonymousReviewersToAuthors(false);
        conference.setAnonymousReviewersToReviewers(false);
        conference.setAnonymousAuthors(false);
        conference.setRestrictedAccessSubmission(false);
        conference.setAssignmentByOrganizer(false);
        */

        /*conference.setKeywords(new ArrayList<>());
        conference.setSubmissions(new ArrayList<>());
        conference.setUsers(new ArrayList<>());
        conference.setOrganizers(new ArrayList<>());
        conference.setNotifications(new ArrayList<>());*/



        // Création de l' organisateur
        Organizer organizer = new Organizer();
        organizer.setConference(conference);
        organizer.setUser(user1);
        organizer.setRole(RoleType.ORGANIZER);
        organizer=organizerRepository.save(organizer);


        // Création du rôle relecteur
        Reviewer reviewer = new Reviewer();
        reviewer.setUser(user2);
        reviewer.setConference(conference);
        reviewer.setRole(RoleType.REVIEWER);
        reviewer = reviewerRepository.save(reviewer);
    }
}

