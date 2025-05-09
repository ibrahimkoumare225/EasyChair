package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.SubType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    
    @Column(name = "creation_date",nullable=false)
    private Date creationDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubType status;
    
    @Column(name = "abstract_sub", length = 4000)
    private String abstractSub;

    @ElementCollection
    @CollectionTable(name = "sub_files", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "file_path")
    private List<String> SubFiles = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "submission_keywords", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;
    
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alert> alerts = new ArrayList<>();


    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL)
    private Evaluation evaluation ;
    
    @ManyToMany(mappedBy = "submissions")
    private List<Author> authors = new ArrayList<>();
    
    @ManyToMany(mappedBy = "evaluatedSubmissions")
    private List<Reviewer> reviewers = new ArrayList<>();
}
