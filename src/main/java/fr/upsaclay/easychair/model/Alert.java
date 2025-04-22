package fr.upsaclay.easychair.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alerts")
@Data
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
