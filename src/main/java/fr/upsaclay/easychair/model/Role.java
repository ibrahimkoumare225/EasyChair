package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Enumerated(EnumType.STRING)
    protected RoleType roleType;

    @Override
    public String toString() {
        return "Role{id=" + id + ", user=" + (user != null ? user.getEmail() : "null") + ", roleType=" + roleType + "}";
    }
    @ManyToOne(optional = false)
    @JoinColumn(name = "conference_id", nullable = false)
    protected Conference conference;
}