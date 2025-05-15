package fr.upsaclay.easychair.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="submission_id")
    private Submission submission;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
}