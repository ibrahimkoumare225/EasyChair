package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.Post;
import fr.upsaclay.easychair.model.Reviewer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reports")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Report extends Post {

    private int grade;

    @Column(length = 4000)
    private String body;

    private int specDegree;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", insertable = false, updatable = false)
    private Reviewer reviewer;
}