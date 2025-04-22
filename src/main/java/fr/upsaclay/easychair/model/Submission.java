package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(name = "creation_date")
    private Date creationDate;
    
    @Enumerated(EnumType.STRING)
    private SubType status;
    
    @Column(name = "abstract_sub", length = 4000)
    private String abstractSub;
    
    @ElementCollection
    @CollectionTable(name = "concrete_sub_files", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "file_path")
    private List<String> concreteSubFiles = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "final_sub_files", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "file_path")
    private List<String> finalSubFiles = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "submission_keywords", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;
    
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alert> alerts = new ArrayList<>();
    
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();
    
    @ManyToMany(mappedBy = "submissions")
    private List<Author> authors = new ArrayList<>();
    
    @ManyToMany(mappedBy = "evaluatedSubmissions")
    private List<Reviewer> reviewers = new ArrayList<>();
}
