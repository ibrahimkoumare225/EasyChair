package fr.upsaclay.easychair.model;

// Reviewer.java
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviewers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reviewer extends Role {
    @OneToMany(mappedBy = "reviewer")
    private List<Alert> sentAlerts = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    private List<Report> reports = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "reviewer_submission",
            joinColumns = @JoinColumn(name = "reviewer_id"),
            inverseJoinColumns = @JoinColumn(name = "submission_id")
    )
    private List<Submission> evaluatedSubmissions = new ArrayList<>();
}