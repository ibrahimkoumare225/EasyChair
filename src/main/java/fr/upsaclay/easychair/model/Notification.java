package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.Conference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Column(name = "sending_date")
    private Date sendingDate;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}