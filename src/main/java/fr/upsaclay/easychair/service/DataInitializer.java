package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.model.enumates.AlertType;
import fr.upsaclay.easychair.model.enumates.Phase;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.model.enumates.SubType;
import fr.upsaclay.easychair.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class DataInitializer {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AlertRepository alertRepository;

    public void initializeData() {
        // Creation de user1
        if (userRepository.findByEmail("john@doe.com").isEmpty()) {
            User user1 = new User();
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setEmail("john@doe.com");
            user1.setPseudo("toto");
            user1.setPassword("password123"); // Valid password (>= 6 characters)
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
            user2.setPassword("password123"); // Valid password (>= 6 characters)
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
            user3.setPassword("alban123"); // Valid password (>= 6 characters)
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
            user4.setPassword("jeremie123"); // Valid password (>= 6 characters)
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
            user5.setPassword("ibrahim123"); // Valid password (>= 6 characters)
            user5.setBirthDate(LocalDate.of(2000, 12, 12));
            user5 = userService.save(user5);
        }

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
        conference.getKeywords().add("test");
        conference = conferenceRepository.save(conference);
        conferenceRepository.flush();

        Conference conference1 = new Conference();
        conference1.setTitle("conf1");
        conference1.setDescription("Techno web.");
        conference1.setCreationDate(LocalDate.now());
        conference1.setCommiteeAssignmentDate(conference1.getCreationDate().plusDays(10));
        conference1.setAbstractSubDate(conference1.getCommiteeAssignmentDate().plusDays(10));
        conference1.setSubAssignmentDate(conference1.getAbstractSubDate().plusDays(10));
        conference1.setConcreteSubDate(conference1.getSubAssignmentDate().plusDays(10));
        conference1.setEvaluationDate(conference1.getConcreteSubDate().plusDays(10));
        conference1.setFinalSubDate(conference1.getEvaluationDate().plusDays(10));
        conference1.setEndDate(conference1.getFinalSubDate().plusDays(10));
        conference1.setPhase(Phase.INITIALIZATION);
        conference1 = conferenceRepository.save(conference1);
        conferenceRepository.flush();

        // Création de l'organisateur
        Organizer organizer = new Organizer();
        organizer.setConference(conference);
        organizer.setUser(userRepository.findByEmail("john@doe.com").orElseThrow());
        organizer.setRole(RoleType.ORGANIZER);
        organizer = organizerRepository.save(organizer);

        // Création du reviewer
        Reviewer reviewer = new Reviewer();
        reviewer.setUser(userRepository.findByEmail("jane@doe.com").orElseThrow());
        reviewer.setConference(conference);
        reviewer.setRole(RoleType.REVIEWER);
        reviewer = reviewerRepository.save(reviewer);

        Reviewer reviewer2 = new Reviewer();
        reviewer2.setUser(userRepository.findByEmail("john@doe.com").orElseThrow());
        reviewer2.setConference(conference);
        reviewer2.setRole(RoleType.REVIEWER);
        reviewer2 = reviewerRepository.save(reviewer2);

        Submission submission1 = new Submission();
        submission1.setTitle("Systèmes intelligents pour la reconnaissance de motifs");
        submission1.setCreationDate(new Date());
        submission1.setStatus(SubType.PROGRESS);
        submission1.setAbstractSub("Cet article traite de la reconnaissance de motifs dans les systèmes d'intelligence artificielle.");
        submission1.setConcreteSubFiles(List.of("soumissions/article1-v1.pdf"));
        submission1.setFinalSubFiles(List.of("soumissions/article1-final.pdf"));
        submission1.setKeywords(Arrays.asList("IA", "Reconnaissance de motifs", "Réseaux de neurones"));
        submission1.setConference(conference);
        submission1 = submissionRepository.save(submission1);

        Submission submission2 = new Submission();
        submission2.setTitle("Exploration de la vie privée dans les réseaux décentralisés");
        submission2.setCreationDate(new Date());
        submission2.setStatus(SubType.PROGRESS);
        submission2.setAbstractSub("Ce résumé aborde les défis liés à la protection de la vie privée dans les systèmes décentralisés.");
        submission2.setConcreteSubFiles(List.of("soumissions/vieprivee-v1.pdf"));
        submission2.setFinalSubFiles(List.of("soumissions/vieprivee-final.pdf"));
        submission2.setKeywords(Arrays.asList("Vie privée", "Blockchain", "Sécurité"));
        submission2.setConference(conference);
        submission2 = submissionRepository.save(submission2);

        Submission submission3 = new Submission();
        submission3.setTitle("Apprentissage profond pour la prévision climatique");
        submission3.setCreationDate(new Date());
        submission3.setStatus(SubType.PROGRESS);
        submission3.setAbstractSub("Ce travail utilise des modèles d'apprentissage profond pour prédire les tendances climatiques.");
        submission3.setConcreteSubFiles(List.of("soumissions/climat-v1.pdf"));
        submission3.setFinalSubFiles(List.of("soumissions/climat-final.pdf"));
        submission3.setKeywords(Arrays.asList("Climat", "Apprentissage profond", "Prévisions"));
        submission3.setConference(conference);
        submission3 = submissionRepository.save(submission3);

        // Auteurs
        Author author1 = new Author();
        author1.setUser(userRepository.findByEmail("alban@cousin.com").orElseThrow());
        author1.setSubmissions(Arrays.asList(submission1, submission2));
        author1.setRole(RoleType.AUTHOR);
        author1 = authorRepository.save(author1);

        Author author2 = new Author();
        author2.setUser(userRepository.findByEmail("ibrahim@koumare.com").orElseThrow());
        author2.setSubmissions(List.of(submission3));
        author2.setRole(RoleType.AUTHOR);
        author2 = authorRepository.save(author2);

        // Création d'évaluation
        Evaluation evaluation = new Evaluation();
        evaluation.setSpecDegree(5);
        evaluation.setGrade(5);
        evaluation.setSubmission(submission1);
        evaluation.setPosts(new ArrayList<>());
        evaluation = evaluationRepository.save(evaluation);

        // Création du Post
        Post post = new Post();
        post.setDate(LocalDateTime.now());
        post.setBody("Le corps du message de post de l'évaluation. Ce post contient des informations importantes concernant l'évaluation.");
        post.setEvaluation(evaluation);
        post.setReviewer(reviewer);
        postRepository.save(post);

        // Création d'une notification
        Notification notif = new Notification();
        notif.setMessage("La conférence est ouverte");
        notif.setSendingDate(new Date());
        notif.setConference(conference);
        notif.setUser(userRepository.findByEmail("john@doe.com").orElseThrow());

        notificationRepository.save(notif);

        // Création d'une alerte
        Alert alert = new Alert();
        alert.setBody("Le contenu de votre soumission n'est pas approprié");
        alert.setType(AlertType.CONTENT_REPORT);
        alert.setSubmission(submission1);
        alert.setReviewer(reviewer);
        alert.setOrganizer(organizer);
        alertRepository.save(alert);
    }
}