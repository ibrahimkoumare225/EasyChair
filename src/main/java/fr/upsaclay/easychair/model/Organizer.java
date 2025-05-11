package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizers")
@Getter
@Setter
@AllArgsConstructor
public class Organizer extends Role {

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Alert> receivedAlerts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    // Constructors
    public Organizer() {
        super();
        setRoleType(RoleType.ORGANIZER); // Définir le type de rôle par défaut
    }

    @Override
    public String toString() {
        return "Organizer{id=" + getId() + ", user=" + (getUser() != null ? getUser().getEmail() : "null") +
                ", conference=" + (conference != null ? conference.getId() : "null") + ", roleType=" + getRoleType() + "}";
    }
}