package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.AlertType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 4000)
    private String body;
    
    @Enumerated(EnumType.STRING)
    private AlertType type;
    
    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;
    
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private Reviewer reviewer;
    
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;
}
