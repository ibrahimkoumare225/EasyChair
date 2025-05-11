package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.Phase;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "commitee_assignment_date")
    private LocalDate commiteeAssignmentDate;

    @Column(name = "abstract_sub_date")
    private LocalDate abstractSubDate;

    @Column(name = "sub_assignment_date")
    private LocalDate subAssignmentDate;

    @Column(name = "concrete_sub_date")
    private LocalDate concreteSubDate;

    @Column(name = "evaluation_date")
    private LocalDate evaluationDate;

    @Column(name = "final_sub_date")
    private LocalDate finalSubDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Phase phase;

    @Column(name = "on_invitation", nullable = false, columnDefinition = "boolean default false")
    private boolean onInvitation;

    @Column(name = "hidden_description", nullable = false, columnDefinition = "boolean default false")
    private boolean hiddenDescription;

    @Column(name = "hidden_conf", nullable = false, columnDefinition = "boolean default false")
    private boolean hiddenConf;

    @Column(name = "anonymous_reviewers_to_authors", nullable = false, columnDefinition = "boolean default false")
    private boolean anonymousReviewersToAuthors;

    @Column(name = "anonymous_reviewers_to_reviewers", nullable = false, columnDefinition = "boolean default false")
    private boolean anonymousReviewersToReviewers;

    @Column(name = "anonymous_authors", nullable = false, columnDefinition = "boolean default false")
    private boolean anonymousAuthors;

    @Column(name = "restricted_access_submission", nullable = false, columnDefinition = "boolean default false")
    private boolean restrictedAccessSubmission;

    @Column(name = "assignment_by_organizer", nullable = false, columnDefinition = "boolean default false")
    private boolean assignmentByOrganizer;

    @ElementCollection
    @CollectionTable(name = "conference_keywords", joinColumns = @JoinColumn(name = "conference_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Organizer> organizers = new ArrayList<>();

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();
}