package fr.upsaclay.easychair.model;

// Author.java
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Author extends Role {
    @ManyToMany
    @JoinTable(
            name = "author_submission",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "submission_id")
    )
    private List<Submission> submissions = new ArrayList<>();
}