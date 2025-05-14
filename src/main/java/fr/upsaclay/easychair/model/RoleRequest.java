package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "role_requests")
@Getter
@Setter
@NoArgsConstructor
public class RoleRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    @Column(nullable = false)
    private String status; // PENDING, ACCEPTED, REJECTED

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    public RoleRequest(User user, Conference conference, RoleType roleType) {
        this.user = user;
        this.conference = conference;
        this.roleType = roleType;
        this.status = "PENDING";
        this.requestDate = LocalDateTime.now();
    }
}