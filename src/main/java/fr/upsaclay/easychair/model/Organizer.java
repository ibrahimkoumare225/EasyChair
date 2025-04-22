package fr.upsaclay.easychair.model;

// Organizer.java
import fr.upsaclay.easychair.model.Conference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Organizer extends Role {
    @OneToMany(mappedBy = "organizer")
    private List<Alert> receivedAlerts = new ArrayList<>();

    @ManyToMany(mappedBy = "organizers")
    private List<Conference> managedConferences = new ArrayList<>();
}