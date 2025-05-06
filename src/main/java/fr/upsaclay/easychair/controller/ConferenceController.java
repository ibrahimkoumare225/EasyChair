package fr.upsaclay.easychair.controller;


import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.service.ConferenceService;
import fr.upsaclay.easychair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    //GET /conferences
    @GetMapping
    public List<Conference> getAllConferences() {
        return conferenceService.findAll();
    }

    //GET /conferences/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Conference> getConferenceByID(@PathVariable Long id) {
        Optional<Conference> conf = conferenceService.findOne(id);
        return conf.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /conferences
    @PostMapping
    public Conference createConference(@RequestBody Conference conference) {
        return conferenceService.save(conference);
    }

    // PUT /conferences
    @PutMapping
    public Conference updateConference(@RequestBody Conference conference) {
        return conferenceService.update(conference);
    }

    // DELETE /conferences/{id}
    @DeleteMapping("/{id}")
    public void deleteConference(@PathVariable Long id) {
        conferenceService.deleteById(id);
    }

    // GET /conferences/search
    @GetMapping("/search")
    public List<Conference> getByTitleIgnoreCaseOrDescriptionIgnoreCase(@RequestBody String title, @RequestBody String description) {
        return conferenceService.findByTitleIgnoreCaseOrDescriptionIgnoreCase(title, description);
    }
    //@Todo
    //A choisir : Plus adapté que la précédente ?
    //GET /conferences/search2
    @GetMapping("/search2")
    public List<Conference> getbySearchTerm(@RequestBody String term) {
        return conferenceService.searchByTerm(term);

    }

}
