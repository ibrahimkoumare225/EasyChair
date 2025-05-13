package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Organizer;
import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.repository.OrganizerRepository;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.repository.AuthorRepository;
import fr.upsaclay.easychair.service.ConferenceService;
import fr.upsaclay.easychair.service.OrganizerService;
import fr.upsaclay.easychair.service.ReviewerService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final OrganizerRepository organizerRepository;
    private final ReviewerRepository reviewerRepository;
    private final AuthorRepository authorRepository;
    private final UserDetailsService userDetailsService;
    private final ReviewerService reviewerService;

    @GetMapping
    public String homePage(Model model, Authentication authentication) {
        logger.debug("Accessing homePage with authentication: {}", authentication);
        try {
            List<Conference> conferences = conferenceService.findAll();
            if (authentication != null && authentication.isAuthenticated()) {
                logger.debug("User authorities: {}", authentication.getAuthorities());
            }
            List<ConferenceView> conferenceViews = conferences.stream().map(conference -> {
                boolean isOrganizer = false;
                if (authentication != null && authentication.isAuthenticated()) {
                    String email = authentication.getName();
                    logger.debug("Checking organizers for conference: {}, user email: {}", conference.getTitle(), email);
                    isOrganizer = conference.getOrganizers().stream()
                            .anyMatch(org -> org.getUser() != null &&
                                    org.getUser().getEmail() != null &&
                                    org.getUser().getEmail().equals(email));
                }
                return new ConferenceView(conference, isOrganizer);
            }).toList();

            model.addAttribute("conferences", conferenceViews);
            return "home";
        } catch (Exception e) {
            logger.error("Error in homePage", e);
            model.addAttribute("error", "An error occurred while loading the home page: " + e.getMessage());
            return "error";
        }
    }

    record ConferenceView(Conference conference, boolean isOrganizer) {}
    record MyConferenceView(Conference conference, List<RoleType> userRoles, LocalDate nextPhaseDate) {}

    @GetMapping("/searchConferences")
    public String searchConferences(@RequestParam(value = "query", required = false) String query, Model model, Authentication authentication) {
        logger.debug("Searching conferences with query: {}", query);
        try {
            List<Conference> conferences;
            if (query != null && !query.trim().isEmpty()) {
                conferences = conferenceService.findByTitleIgnoreCaseOrDescriptionIgnoreCase(query, query, query);
            } else {
                conferences = conferenceService.findAll();
            }
            List<ConferenceView> conferenceViews = conferences.stream().map(conference -> {
                boolean isOrganizer = false;
                if (authentication != null && authentication.isAuthenticated()) {
                    String email = authentication.getName();
                    isOrganizer = conference.getOrganizers().stream()
                            .anyMatch(org -> org.getUser() != null &&
                                    org.getUser().getEmail() != null &&
                                    org.getUser().getEmail().equals(email));
                }
                return new ConferenceView(conference, isOrganizer);
            }).toList();

            model.addAttribute("conferences", conferenceViews);
            model.addAttribute("query", query);
            return "home";
        } catch (Exception e) {
            logger.error("Error in searchConferences", e);
            model.addAttribute("error", "An error occurred while searching conferences: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping
    @Transactional
    public String saveConference(@ModelAttribute Conference conference,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        logger.debug("Saving new conference: {}", conference);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to save conference");
            return "redirect:/login";
        }

        try {
            if (conference.getCreationDate() == null) {
                conference.setCreationDate(LocalDate.now());
            }

            Conference savedConference = conferenceService.save(conference);

            String email = authentication.getName();
            logger.debug("Authenticated user email for saveConference: {}", email);
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                logger.error("User not found for email: {}", email);
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/conference";
            }

            User user = userOptional.get();
            Organizer organizer = new Organizer();
            organizer.setUser(user);
            organizer.setConference(savedConference);

            organizerService.save(organizer);
            logger.debug("Organizer saved for user: {}, conference: {}", email, savedConference.getId());

            UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(email);
            Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    updatedUserDetails, authentication.getCredentials(), updatedUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            logger.debug("Security context updated with authorities: {}", newAuth.getAuthorities());

            logger.info("Conference created successfully with ID: {}", savedConference.getId());
            redirectAttributes.addFlashAttribute("message", "Conference created successfully!");
            return "redirect:/conference";
        } catch (Exception e) {
            logger.error("Error saving conference", e);
            redirectAttributes.addFlashAttribute("error", "Failed to create conference: " + e.getMessage());
            return "redirect:/conference";
        }
    }

    @PostMapping("/update")
    @Transactional
    public String updateConference(@ModelAttribute Conference conference,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        logger.debug("Updating conference with ID: {}", conference.getId());

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to update conference");
            return "redirect:/login";
        }

        try {
            if (conference.getId() == null) {
                logger.error("Conference ID is null during update");
                redirectAttributes.addFlashAttribute("error", "Invalid conference ID.");
                return "redirect:/conference";
            }

            Optional<Conference> existingConference = conferenceService.findOne(conference.getId());
            if (existingConference.isEmpty()) {
                logger.warn("Conference not found for ID: {}", conference.getId());
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/conference";
            }

            logger.debug("Conference data before update: {}", conference);
            Conference updatedConference = conferenceService.update(conference);
            logger.info("Conference updated successfully with ID: {}", updatedConference.getId());
            redirectAttributes.addFlashAttribute("message", "Conference updated successfully!");
            return "redirect:/conference";
        } catch (Exception e) {
            logger.error("Error updating conference with ID: {}", conference.getId(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to update conference: " + e.getMessage());
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
            model.addAttribute("error", "An error occurred while loading the add conference form: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/conference/{id}")
    public String showUpdateConferenceForms(Model model, @PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Accessing update conference form for ID: {}", id);
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to update conference");
            redirectAttributes.addFlashAttribute("error", "You must be logged in to update a conference.");
            return "redirect:/login";
        }

        try {
            Optional<Conference> conferenceOptional = conferenceService.findOne(id);
            if (conferenceOptional.isEmpty()) {
                logger.warn("Conference not found for ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/conference";
            }

            model.addAttribute("conference", conferenceOptional.get());
            return "dynamic/conference/createConference";
        } catch (Exception e) {
            logger.error("Error in showUpdateConferenceForms", e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while accessing the conference: " + e.getMessage());
            return "redirect:/conference";
        }
    }

    @GetMapping("/conferenceDetail/{id}")
    public String showDetailConferenceForms(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.debug("Accessing conference detail for ID: {}", id);
        try {
            Optional<Conference> conference = conferenceService.findOne(id);
            if (conference.isPresent()) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userService.findByEmail(email).orElseThrow();
                Optional<Reviewer> reviewer = reviewerService.findByUserId(user.getId());
                if(reviewer.isPresent()) {
                    model.addAttribute("authorized", true);
                }else {
                    model.addAttribute("authorized", false);
                }
                model.addAttribute("conference", conference.get());
                return "dynamic/conference/detailConference";
            } else {
                logger.warn("Conference not found for ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/conference";
            }
        } catch (Exception e) {
            logger.error("Error in showDetailConferenceForms", e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while accessing the conference details: " + e.getMessage());
            return "redirect:/conference";
        }
    }

    @PostMapping("/deleteConference/{id}")
    public String deleteConference(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Attempting to delete conference with ID: {}", id);
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to delete conference");
            redirectAttributes.addFlashAttribute("error", "You must be logged in to delete a conference.");
            return "redirect:/login";
        }

        try {
            Optional<Conference> conferenceOptional = conferenceService.findOne(id);
            if (conferenceOptional.isEmpty()) {
                logger.warn("Conference not found for ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/conference";
            }

            Conference conference = conferenceOptional.get();
            String email = authentication.getName();
            logger.debug("Authenticated user email: {}", email);

            logger.debug("Organizers for conference {}: {}", id, conference.getOrganizers());
            for (Organizer org : conference.getOrganizers()) {
                logger.debug("Organizer ID: {}, User: {}, Conference ID: {}, RoleType: {}",
                        org.getId(), org.getUser() != null ? org.getUser().getEmail() : "null",
                        org.getConference() != null ? org.getConference().getId() : "null",
                        org.getRoleType());
            }

            for (Organizer organizer : List.copyOf(conference.getOrganizers())) {
                logger.debug("Deleting organizer with ID: {} for conference: {}", organizer.getId(), id);
                organizerRepository.deleteById(organizer.getId());
            }

            List<Reviewer> reviewers = reviewerRepository.findByConferenceId(id);
            for (Reviewer reviewer : reviewers) {
                logger.debug("Deleting reviewer with ID: {} for conference: {}", reviewer.getId(), id);
                reviewerRepository.deleteById(reviewer.getId());
            }

            List<Author> authors = authorRepository.findByConferenceId(id);
            for (Author author : authors) {
                logger.debug("Deleting author with ID: {} for conference: {}", author.getId(), id);
                authorRepository.deleteById(author.getId());
            }

            logger.debug("Deleting conference with ID: {}", id);
            conferenceService.deleteById(id);

            logger.info("Conference with ID {} deleted successfully", id);
            redirectAttributes.addFlashAttribute("message", "Conference deleted successfully!");
            return "redirect:/conference";
        } catch (Exception e) {
            logger.error("Error deleting conference with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete conference: " + e.getMessage());
            return "redirect:/conference";
        }
    }

    @GetMapping("/post-login")
    public String postLogin(Authentication authentication, RedirectAttributes redirectAttributes, Model model) {
        logger.debug("Entering post-login with authentication: {}", authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to access post-login");
            return "redirect:/login";
        }
        try {
            logger.debug("User authorities after login: {}", authentication.getAuthorities());
            redirectAttributes.addFlashAttribute("message", "Successfully logged in!");
            return "redirect:/conference";
        } catch (Exception e) {
            logger.error("Error in postLogin", e);
            model.addAttribute("error", "An error occurred during post-login: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/myConference")
    public String myConference(Model model, Authentication authentication) {
        logger.debug("Accessing myConference with authentication: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to access myConference");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            logger.debug("Fetching conferences for user email: {}", email);

            List<Conference> conferences = conferenceService.findConferencesByUserEmail(email);

            List<MyConferenceView> conferenceViews = conferences.stream().map(conference -> {
                List<RoleType> userRoles = new ArrayList<>();

                boolean isOrganizer = conference.getOrganizers().stream()
                        .anyMatch(org -> org.getUser() != null && email.equals(org.getUser().getEmail()));
                if (isOrganizer) {
                    userRoles.add(RoleType.ORGANIZER);
                }

                boolean isReviewer = reviewerRepository.findByConferenceIdAndUserEmail(conference.getId(), email).isPresent();
                if (isReviewer) {
                    userRoles.add(RoleType.REVIEWER);
                }

                boolean isAuthor = authorRepository.findByConferenceIdAndUserEmail(conference.getId(), email).isPresent();
                if (isAuthor) {
                    userRoles.add(RoleType.AUTHOR);
                }

                LocalDate nextPhaseDate = getNextPhaseDate(conference);

                return new MyConferenceView(conference, userRoles, nextPhaseDate);
            }).toList();

            model.addAttribute("conferenceViews", conferenceViews);
            return "dynamic/conference/myConference";
        } catch (Exception e) {
            logger.error("Error in myConference", e);
            model.addAttribute("error", "An error occurred while loading your conferences: " + e.getMessage());
            return "error";
        }
    }

    private LocalDate getNextPhaseDate(Conference conference) {
        switch (conference.getPhase()) {
            case INITIALIZATION:
                return conference.getCommiteeAssignmentDate();
            case COMMITEE_ASSIGNMENT:
                return conference.getAbstractSubDate();
            case ABSTRACT_SUBMISSION:
                return conference.getSubAssignmentDate();
            case SUBMISSION_ASSIGNMENT:
                return conference.getConcreteSubDate();
            case CONCRETE_SUBMISSION:
                return conference.getEvaluationDate();
            case EVALUATION:
                return conference.getFinalSubDate();
            case FINAL_SUBMISSION:
                return conference.getEndDate();
            case VALIDATION:
                return conference.getEndDate();
            case CLOSED:
            default:
                return null;
        }
    }
}