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
public class Organizer extends Role {

    @OneToMany(mappedBy = "organizer")
    private List<Alert> receivedAlerts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="conference_id",nullable = false)
    private Conference conference;
}