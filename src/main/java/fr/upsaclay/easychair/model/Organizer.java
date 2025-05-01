package fr.upsaclay.easychair.model;

// Organizer.java
import fr.upsaclay.easychair.model.Conference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizers")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Organizer extends User {

    @OneToMany(mappedBy = "organizer")
    private List<Alert> receivedAlerts = new ArrayList<>();

    @ManyToMany(mappedBy = "organizers")
    private List<Conference> managedConferences = new ArrayList<>();
}