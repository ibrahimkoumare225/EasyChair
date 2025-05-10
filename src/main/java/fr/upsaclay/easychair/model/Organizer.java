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

    public Organizer() {
        this.setRole(RoleType.ORGANIZER);
    }
}