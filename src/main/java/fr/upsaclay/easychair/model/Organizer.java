package fr.upsaclay.easychair.model;

import fr.upsaclay.easychair.model.enumates.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizers")
@Getter
@Setter
@AllArgsConstructor
public class Organizer extends Role {

    @ManyToOne
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Alert> receivedAlerts = new ArrayList<>();

    public Organizer() {
        this.setRole(RoleType.ORGANIZER);
    }
}