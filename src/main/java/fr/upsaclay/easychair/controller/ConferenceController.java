package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Organizer;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.service.ConferenceService;
import fr.upsaclay.easychair.service.OrganizerService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/conference")
public class ConferenceController {

    private static final Logger logger = LoggerFactory.getLogger(ConferenceController.class);

    private final ConferenceService conferenceService;
    private final UserService userService;
    private final OrganizerService organizerService;

    @GetMapping
    public String homePage(Model model, Authentication authentication) {
        logger.debug("Accessing homePage with authentication: {}", authentication);
        try {
            List<Conference> conferences = conferenceService.findAll();
            model.addAttribute("conferences", conferences);
            if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
                model.addAttribute("user", authentication.getPrincipal());
            }
            return "home";
        } catch (Exception e) {
            logger.error("Error in homePage", e);
            throw e; // Let the error controller handle it
        }
    }

    @GetMapping("/searchConferences")
    public String searchConferences(@RequestParam(value = "query", required = false) String query, Model model) {
        logger.debug("Searching conferences with query: {}", query);
        try {
            List<Conference> conferences;
            if (query != null && !query.trim().isEmpty()) {
                conferences = conferenceService.findByTitleIgnoreCaseOrDescriptionIgnoreCase(query, query, query);
            } else {
                conferences = conferenceService.findAll();
            }
            model.addAttribute("conferences", conferences);
            model.addAttribute("query", query);
            return "home";
        } catch (Exception e) {
            logger.error("Error in searchConferences", e);
            throw e;
        }
    }

    @PostMapping
    public String saveConference(@ModelAttribute Conference conference,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        logger.debug("Saving conference: {}", conference);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to save conference");
            return "redirect:/login";
        }

        try {
            // Set creation date if not set
            if (conference.getCreationDate() == null) {
                conference.setCreationDate(LocalDate.now());
            }

            // Save the conference first
            Conference savedConference = conferenceService.save(conference);

            // Get the authenticated user
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);

            if (userOptional.isEmpty()) {
                logger.error("User not found for email: {}", email);
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/conference";
            }

            User user = userOptional.get();

            // Create and assign ORGANIZER role
            Organizer organizer = new Organizer();
            organizer.setRole(RoleType.ORGANIZER);
            organizer.setUser(user);
            organizer.setConference(savedConference);

            // Save the organizer
            organizer = organizerService.save(organizer);

            // Add organizer to conference's organizers list
            savedConference.getOrganizers().add(organizer);
            conferenceService.update(savedConference);

            redirectAttributes.addFlashAttribute("message", "Conference created successfully!");
            return "redirect:/conference";
        } catch (Exception e) {
            logger.error("Error saving conference", e);
            redirectAttributes.addFlashAttribute("error", "Failed to create conference.");
            return "redirect:/conference";
        }
    }

    @GetMapping("/ajouterConference")
    public String showAddConferenceForms(Model model, Authentication authentication) {
        logger.debug("Accessing ajouterConference with authentication: {}", authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to access ajouterConference");
            return "redirect:/login";
        }
        try {
            model.addAttribute("conference", new Conference());
            return "dynamic/conference/createConference";
        } catch (Exception e) {
            logger.error("Error in showAddConferenceForms", e);
            throw e;
        }
    }

    @GetMapping("/conference/{id}")
    public String showUpdateConferenceForms(Model model, @PathVariable Long id, Authentication authentication) {
        logger.debug("Accessing update conference form for ID: {}", id);
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to update conference");
            return "redirect:/login";
        }

        try {
            Optional<Conference> conferenceOptional = conferenceService.findOne(id);
            if (conferenceOptional.isEmpty()) {
                logger.warn("Conference not found for ID: {}", id);
                return "redirect:/home";
            }

            Conference conference = conferenceOptional.get();
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                logger.error("User not found for email: {}", email);
                return "redirect:/home";
            }

            User user = userOptional.get();
            boolean isOrganizer = conference.getOrganizers().stream()
                    .anyMatch(org -> org.getUser().getId().equals(user.getId()));
            if (!isOrganizer) {
                logger.warn("User {} is not an organizer for conference {}", email, id);
                return "redirect:/error?status=403";
            }

            model.addAttribute("conference", conference);
            return "dynamic/conference/createConference";
        } catch (Exception e) {
            logger.error("Error in showUpdateConferenceForms", e);
            throw e;
        }
    }

    @GetMapping("/conferenceDetail/{id}")
    public String showDetailConferenceForms(Model model, @PathVariable Long id) {
        logger.debug("Accessing conference detail for ID: {}", id);
        try {
            Optional<Conference> conference = conferenceService.findOne(id);
            if (conference.isPresent()) {
                model.addAttribute("conference", conference.get());
                return "dynamic/conference/detailConference";
            } else {
                logger.warn("Conference not found for ID: {}", id);
                return "redirect:/home";
            }
        } catch (Exception e) {
            logger.error("Error in showDetailConferenceForms", e);
            throw e;
        }
    }

    @PostMapping("/deleteConference/{id}")
    public String deleteConference(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Deleting conference with ID: {}", id);
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to delete conference");
            return "redirect:/login";
        }

        try {
            Optional<Conference> conferenceOptional = conferenceService.findOne(id);
            if (conferenceOptional.isEmpty()) {
                logger.warn("Conference not found for ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/home";
            }

            Conference conference = conferenceOptional.get();
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                logger.error("User not found for email: {}", email);
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/home";
            }

            User user = userOptional.get();
            boolean isOrganizer = conference.getOrganizers().stream()
                    .anyMatch(org -> org.getUser().getId().equals(user.getId()));
            if (!isOrganizer) {
                logger.warn("User {} is not an organizer for conference {}", email, id);
                redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this conference.");
                return "redirect:/error?status=403";
            }

            conferenceService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Conference deleted successfully!");
            return "redirect:/home";
        } catch (Exception e) {
            logger.error("Error deleting conference", e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete conference.");
            return "redirect:/home";
        }
    }
}