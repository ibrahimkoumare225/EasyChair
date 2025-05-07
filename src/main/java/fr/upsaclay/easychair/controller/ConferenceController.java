
package fr.upsaclay.easychair.controller;


import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Controller
@RequestMapping("/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @GetMapping
    public String homePage(Model model) {
        List<Conference> conferences = conferenceService.findAll();
        model.addAttribute("conferences",conferences);
        return "home";
    }
    @GetMapping("/searchConferences")
    public String searchConferences(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Conference> conferences;
        if (query != null && !query.trim().isEmpty()) {
            conferences = conferenceService.findByTitleIgnoreCaseOrDescriptionIgnoreCase(query, query);
        } else {
            conferences = conferenceService.findAll(); // Return all conferences if query is empty
        }
        model.addAttribute("conferences", conferences);
        model.addAttribute("query", query);
        return "home"; // Renders src/main/resources/templates/home.html
    }
    @PostMapping
    public String saveConference(Conference conference) {
        conferenceService.save(conference);
        return "redirect:/conference";
    }

    @GetMapping("/ajouterConference")
    public String showAddConferenceForms(Model model) {
        model.addAttribute("conference", new Conference());
        return "dynamic/conference/createConference";
    }

    @GetMapping("/updateProfesseur")
    public String updateConference() {
        return "dynamic/conference/updateConference";
    }

    @GetMapping("/conference/{id}")
    public String showUpdateConferenreForms(Model model, @PathVariable Long id) {
        Optional<Conference> conference = conferenceService.findOne(id);
        if (conference.isPresent()) {
            model.addAttribute("conference", conference.get());
            return "dynamic/conference/createConference";
        } else {
            return "redirect:/home";
        }
    }
    @GetMapping("/conferenceDetail/{id}")
    public String showDetailConferenreForms(Model model, @PathVariable Long id) {
        Optional<Conference> conference = conferenceService.findOne(id);
        if (conference.isPresent()) {
            model.addAttribute("conference", conference.get());
            return "dynamic/conference/detailConference";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/deleteConference/{id}")
    public String deleteProfesseur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        conferenceService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Conference deleted successfully!");
        return "redirect:/home";
    }


}