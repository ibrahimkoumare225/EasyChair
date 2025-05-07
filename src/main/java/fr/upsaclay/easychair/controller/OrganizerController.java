/*
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Organizer;
import fr.upsaclay.easychair.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class OrganizerController {


    private final OrganizerService organizerService;

    // GET /organizers
    @GetMapping
    public List<Organizer> getAllOrganizers() {
        return OrganizerController.this.organizerService.findAll();
    }

    // GET /organizers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Organizer> getOrganizerById(@PathVariable Long id) {
        Optional<Organizer> organizer = organizerService.findOne(id);
        return organizer.isPresent() ? (ResponseEntity<Organizer>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /organizers
    @PostMapping
    public Organizer createOrganizer(@RequestBody Organizer organizer) {
        return organizerService.save(organizer);
    }

    // PUT /organizers/{id}
    @PutMapping
    public Organizer updateOrganizer(@RequestBody Organizer organizer) {
        return OrganizerController.this.organizerService.update(organizer);
    }

    // DELETE /organizers/{id}
    @DeleteMapping("/{id}")
    public void deleteOrganizer(@PathVariable Long id) {
        OrganizerController.this.organizerService.delete(id);
    }

}
*/
