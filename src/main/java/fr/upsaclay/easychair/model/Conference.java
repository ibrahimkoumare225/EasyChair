package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.Phase;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
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
    private  Long id;
    
    private String title;
    private String description;
    
    @Column(name = "creation_date")
    private Date creationDate;
    
    @Column(name = "commitee_assignment_date")
    private Date commiteeAssignmentDate;
    
    @Column(name = "abstract_sub_date")
    private Date abstractSubDate;
    
    @Column(name = "sub_assignment_date")
    private Date subAssignmentDate;
    
    @Column(name = "concrete_sub_date")
    private Date concreteSubDate;
    
    @Column(name = "evaluation_date")
    private Date evaluationDate;
    
    @Column(name = "final_sub_date")
    private Date finalSubDate;
    
    @Column(name = "end_date")
    private Date endDate;
    
    @Enumerated(EnumType.STRING)
    private Phase phase;
    
    private boolean onInvitation;
    private boolean hiddenDescription;
    private boolean hiddenConf;
    private boolean anonymousReviewersToAuthors;
    private boolean anonymousReviewersToReviewers;
    private boolean anonymousAuthors;
    private boolean restrictedAccessSubmission;
    private boolean assignmentByOrganizer;
    
    @ElementCollection
    @CollectionTable(name = "conference_keywords", joinColumns = @JoinColumn(name = "conference_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();
    
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions = new ArrayList<>();
    
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "conference_organizer",
        joinColumns = @JoinColumn(name = "conference_id"),
        inverseJoinColumns = @JoinColumn(name = "organizer_id")
    )
    private List<Organizer> organizers = new ArrayList<>();
    
    @OneToMany(mappedBy = "conference")
    private List<Notification> notifications = new ArrayList<>();
}
