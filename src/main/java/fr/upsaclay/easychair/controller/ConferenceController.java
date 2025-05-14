package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.repository.OrganizerRepository;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.repository.AuthorRepository;
import fr.upsaclay.easychair.repository.RoleRequestRepository;
import fr.upsaclay.easychair.service.*;
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
    private final RoleRequestService roleRequestService;
    private final UserDetailsService userDetailsService;
    private final ReviewerService reviewerService;
    private final AuthorService authorService;


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
                boolean hasAuthorRole = false;
                boolean hasReviewerRole = false;
                boolean hasPendingAuthorRequest = false;
                boolean hasPendingReviewerRequest = false;
                if (authentication != null && authentication.isAuthenticated()) {
                    String email = authentication.getName();
                    logger.debug("Checking roles for conference: {}, user email: {}", conference.getTitle(), email);
                    isOrganizer = conference.getOrganizers().stream()
                            .anyMatch(org -> org.getUser() != null &&
                                    org.getUser().getEmail() != null &&
                                    org.getUser().getEmail().equals(email));
                    hasAuthorRole = authorRepository.findByConferenceIdAndUserEmail(conference.getId(), email).isPresent();
                    hasReviewerRole = reviewerRepository.findByConferenceIdAndUserEmail(conference.getId(), email).isPresent();
                    Optional<User> userOptional = userService.findByEmail(email);
                    if (userOptional.isPresent()) {
                        Long userId = userOptional.get().getId();
                        hasPendingAuthorRequest = roleRequestService.findByUserIdAndConferenceIdAndRoleType(userId, conference.getId(), RoleType.AUTHOR)
                                .map(request -> request.getStatus().equals("PENDING"))
                                .orElse(false);
                        hasPendingReviewerRequest = roleRequestService.findByUserIdAndConferenceIdAndRoleType(userId, conference.getId(), RoleType.REVIEWER)
                                .map(request -> request.getStatus().equals("PENDING"))
                                .orElse(false);
                    }
                }
                return new ConferenceView(conference, isOrganizer, hasAuthorRole, hasReviewerRole, hasPendingAuthorRequest, hasPendingReviewerRequest);
            }).toList();

            model.addAttribute("conferences", conferenceViews);
            return "home";
        } catch (Exception e) {
            logger.error("Error in homePage", e);
            model.addAttribute("error", "An error occurred while loading the home page: " + e.getMessage());
            return "error";
        }
    }

    record ConferenceView(
            Conference conference,
            boolean isOrganizer,
            boolean hasAuthorRole,
            boolean hasReviewerRole,
            boolean hasPendingAuthorRequest,
            boolean hasPendingReviewerRequest
    ) {
    }

    record MyConferenceView(Conference conference, List<RoleType> userRoles, LocalDate nextPhaseDate) {
    }

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
                boolean hasAuthorRole = false;
                boolean hasReviewerRole = false;
                boolean hasPendingAuthorRequest = false;
                boolean hasPendingReviewerRequest = false;
                if (authentication != null && authentication.isAuthenticated()) {
                    String email = authentication.getName();
                    logger.debug("Checking roles for conference: {}, user email: {}", conference.getTitle(), email);
                    isOrganizer = conference.getOrganizers().stream()
                            .anyMatch(org -> org.getUser() != null &&
                                    org.getUser().getEmail() != null &&
                                    org.getUser().getEmail().equals(email));
                    hasAuthorRole = authorRepository.findByConferenceIdAndUserEmail(conference.getId(), email).isPresent();
                    hasReviewerRole = reviewerRepository.findByConferenceIdAndUserEmail(conference.getId(), email).isPresent();
                    Optional<User> userOptional = userService.findByEmail(email);
                    if (userOptional.isPresent()) {
                        Long userId = userOptional.get().getId();
                        hasPendingAuthorRequest = roleRequestService.findByUserIdAndConferenceIdAndRoleType(userId, conference.getId(), RoleType.AUTHOR)
                                .map(request -> request.getStatus().equals("PENDING"))
                                .orElse(false);
                        hasPendingReviewerRequest = roleRequestService.findByUserIdAndConferenceIdAndRoleType(userId, conference.getId(), RoleType.REVIEWER)
                                .map(request -> request.getStatus().equals("PENDING"))
                                .orElse(false);
                    }
                }
                return new ConferenceView(conference, isOrganizer, hasAuthorRole, hasReviewerRole, hasPendingAuthorRequest, hasPendingReviewerRequest);
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
    public String showDetailConferenceForms(Model model, @PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Accessing conference detail for ID: {}", id);
        try {
            Optional<Conference> conferenceOptional = conferenceService.findOne(id);
            if (conferenceOptional.isEmpty()) {
                logger.warn("Conference not found for ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/conference";
            }

            Conference conference = conferenceOptional.get();
            model.addAttribute("conference", conference);

            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();

                boolean isOrganizer = conference.getOrganizers().stream()
                        .anyMatch(org -> org.getUser() != null && email.equals(org.getUser().getEmail()));
                boolean isReviewer = reviewerRepository.findByConferenceIdAndUserEmail(id, email).isPresent();
                boolean isAuthor = authorRepository.findByConferenceIdAndUserEmail(id, email).isPresent();

                model.addAttribute("isOrganizer", isOrganizer);
                model.addAttribute("isReviewer", isReviewer);
                model.addAttribute("isAuthor", isAuthor);

                Optional<User> userOptional = userService.findByEmail(email);
                if (userOptional.isPresent()) {
                    Long userId = userOptional.get().getId();
                    Optional<RoleRequest> authorRequest = roleRequestService.findByUserIdAndConferenceIdAndRoleType(userId, id, RoleType.AUTHOR);
                    Optional<RoleRequest> reviewerRequest = roleRequestService.findByUserIdAndConferenceIdAndRoleType(userId, id, RoleType.REVIEWER);

                    model.addAttribute("hasPendingAuthorRequest", authorRequest.isPresent() && "PENDING".equals(authorRequest.get().getStatus()));
                    model.addAttribute("hasPendingReviewerRequest", reviewerRequest.isPresent() && "PENDING".equals(reviewerRequest.get().getStatus()));
                }
            }

            return "dynamic/conference/detailConference";

        } catch(Exception e){
            logger.error("Error in showDetailConferenceForms", e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while accessing the conference details: " + e.getMessage());
            return "redirect:/conference";
        }
    }

    @PostMapping("/requestRole/{conferenceId}")
    @Transactional
    public String requestRole(@PathVariable Long conferenceId, @RequestParam String roleType, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Requesting role {} for conference ID: {}", roleType, conferenceId);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to request role");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                logger.error("User not found for email: {}", email);
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/conference/conferenceDetail/" + conferenceId;
            }

            Optional<Conference> conferenceOptional = conferenceService.findOne(conferenceId);
            if (conferenceOptional.isEmpty()) {
                logger.warn("Conference not found for ID: {}", conferenceId);
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/conference";
            }

            User user = userOptional.get();
            Conference conference = conferenceOptional.get();
            RoleType requestedRoleType = RoleType.valueOf(roleType);

            // Vérifier si l'utilisateur a déjà ce rôle
            if (requestedRoleType == RoleType.AUTHOR && authorRepository.findByConferenceIdAndUserEmail(conferenceId, email).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "You already have the Author role.");
                return "redirect:/conference/conferenceDetail/" + conferenceId;
            }
            if (requestedRoleType == RoleType.REVIEWER && reviewerRepository.findByConferenceIdAndUserEmail(conferenceId, email).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "You already have the Reviewer role.");
                return "redirect:/conference/conferenceDetail/" + conferenceId;
            }

            // Vérifier si une demande en cours existe
            Optional<RoleRequest> existingRequest = roleRequestService.findByUserIdAndConferenceIdAndRoleType(user.getId(), conferenceId, requestedRoleType);
            if (existingRequest.isPresent() && existingRequest.get().getStatus().equals("PENDING")) {
                redirectAttributes.addFlashAttribute("error", "You already have a pending request for this role.");
                return "redirect:/conference/conferenceDetail/" + conferenceId;
            }

            // Créer une nouvelle demande
            RoleRequest roleRequest = new RoleRequest(user, conference, requestedRoleType);
            roleRequestService.save(roleRequest);

            logger.info("Role request created for user: {}, role: {}, conference: {}", email, roleType, conferenceId);
            redirectAttributes.addFlashAttribute("message", "Role request submitted successfully!");
            return "redirect:/conference/conferenceDetail/" + conferenceId;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid role type: {}", roleType, e);
            redirectAttributes.addFlashAttribute("error", "Invalid role type: " + roleType);
            return "redirect:/conference/conferenceDetail/" + conferenceId;
        } catch (Exception e) {
            logger.error("Error requesting role for conference ID: {}", conferenceId, e);
            redirectAttributes.addFlashAttribute("error", "Failed to submit role request: " + e.getMessage());
            return "redirect:/conference/conferenceDetail/" + conferenceId;
        }
    }

    @PostMapping("/acceptRoleRequest/{requestId}")
    @Transactional
    public String acceptRoleRequest(@PathVariable Long requestId, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Accepting role request ID: {}", requestId);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to accept role request");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            Optional<RoleRequest> requestOptional = roleRequestService.findOne(requestId);
            if (requestOptional.isEmpty()) {
                logger.warn("Role request not found for ID: {}", requestId);
                redirectAttributes.addFlashAttribute("error", "Role request not found.");
                return "redirect:/conference/myNotification";
            }

            RoleRequest roleRequest = requestOptional.get();
            Conference conference = roleRequest.getConference();

            // Vérifier si l'utilisateur est un organisateur
            boolean isOrganizer = conference.getOrganizers().stream()
                    .anyMatch(org -> org.getUser() != null && email.equals(org.getUser().getEmail()));
            if (!isOrganizer) {
                logger.warn("User {} is not an organizer for conference ID: {}", email, conference.getId());
                redirectAttributes.addFlashAttribute("error", "You are not authorized to accept this request.");
                return "redirect:/conference/myNotification";
            }

            // Mettre à jour le statut de la demande
            roleRequest.setStatus("ACCEPTED");
            roleRequestService.save(roleRequest);

            // Ajouter le rôle à l'utilisateur
            User user = roleRequest.getUser();
            if (roleRequest.getRoleType() == RoleType.AUTHOR) {
                Author author = new Author();
                author.setUser(user);
                author.setConference(conference);
                author.setRoleType(RoleType.AUTHOR);
                authorService.save(author);
            } else if (roleRequest.getRoleType() == RoleType.REVIEWER) {
                Reviewer reviewer = new Reviewer();
                reviewer.setUser(user);
                reviewer.setConference(conference);
                reviewer.setRoleType(RoleType.REVIEWER);
                reviewerService.save(reviewer);
            }

            logger.info("Role request ID: {} accepted for user: {}, role: {}", requestId, user.getEmail(), roleRequest.getRoleType());
            redirectAttributes.addFlashAttribute("message", "Role request accepted successfully!");
            return "redirect:/conference/myNotification";
        } catch (Exception e) {
            logger.error("Error accepting role request ID: {}", requestId, e);
            redirectAttributes.addFlashAttribute("error", "Failed to accept role request: " + e.getMessage());
            return "redirect:/conference/myNotification";
        }
    }

    @PostMapping("/rejectRoleRequest/{requestId}")
    @Transactional
    public String rejectRoleRequest(@PathVariable Long requestId, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Rejecting role request ID: {}", requestId);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to reject role request");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            Optional<RoleRequest> requestOptional = roleRequestService.findOne(requestId);
            if (requestOptional.isEmpty()) {
                logger.warn("Role request not found for ID: {}", requestId);
                redirectAttributes.addFlashAttribute("error", "Role request not found.");
                return "redirect:/conference/myNotification";
            }

            RoleRequest roleRequest = requestOptional.get();
            Conference conference = roleRequest.getConference();

            // Vérifier si l'utilisateur est un organisateur
            boolean isOrganizer = conference.getOrganizers().stream()
                    .anyMatch(org -> org.getUser() != null && email.equals(org.getUser().getEmail()));
            if (!isOrganizer) {
                logger.warn("User {} is not an organizer for conference ID: {}", email, conference.getId());
                redirectAttributes.addFlashAttribute("error", "You are not authorized to reject this request.");
                return "redirect:/conference/myNotification";
            }

            // Mettre à jour le statut de la demande
            roleRequest.setStatus("REJECTED");
            roleRequestService.save(roleRequest);

            logger.info("Role request ID: {} rejected for user: {}, role: {}", requestId, roleRequest.getUser().getEmail(), roleRequest.getRoleType());
            redirectAttributes.addFlashAttribute("message", "Role request rejected successfully!");
            return "redirect:/conference/myNotification";
        } catch (Exception e) {
            logger.error("Error rejecting role request ID: {}", requestId, e);
            redirectAttributes.addFlashAttribute("error", "Failed to reject role request: " + e.getMessage());
            return "redirect:/conference/myNotification";
        }
    }

    @PostMapping("/cancelRoleRequest/{requestId}")
    @Transactional
    public String cancelRoleRequest(@PathVariable Long requestId, Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Cancelling role request ID: {}", requestId);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to cancel role request");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            Optional<RoleRequest> requestOptional = roleRequestService.findOne(requestId);
            if (requestOptional.isEmpty()) {
                logger.warn("Role request not found for ID: {}", requestId);
                redirectAttributes.addFlashAttribute("error", "Role request not found.");
                return "redirect:/conference/myRoleRequests";
            }

            RoleRequest roleRequest = requestOptional.get();
            if (!roleRequest.getUser().getEmail().equals(email)) {
                logger.warn("User {} is not authorized to cancel role request ID: {}", email, requestId);
                redirectAttributes.addFlashAttribute("error", "You are not authorized to cancel this request.");
                return "redirect:/conference/myRoleRequests";
            }

            if (!roleRequest.getStatus().equals("PENDING")) {
                logger.warn("Role request ID: {} is not in PENDING state", requestId);
                redirectAttributes.addFlashAttribute("error", "Only pending requests can be cancelled.");
                return "redirect:/conference/myRoleRequests";
            }

            roleRequestService.delete(requestId);

            logger.info("Role request ID: {} cancelled by user: {}", requestId, email);
            redirectAttributes.addFlashAttribute("message", "Role request cancelled successfully!");
            return "redirect:/conference/myRoleRequests";
        } catch (Exception e) {
            logger.error("Error cancelling role request ID: {}", requestId, e);
            redirectAttributes.addFlashAttribute("error", "Failed to cancel role request: " + e.getMessage());
            return "redirect:/conference/myRoleRequests";
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

    @GetMapping("/myNotification")
    public String myNotification(Model model, Authentication authentication) {
        logger.debug("Accessing myNotification with authentication: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to access myNotification");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            logger.debug("Fetching notifications for user email: {}", email);

            // Trouver les conférences où l'utilisateur est organisateur
            List<Conference> organizedConferences = conferenceService.findConferencesByUserEmail(email).stream()
                    .filter(conference -> conference.getOrganizers().stream()
                            .anyMatch(org -> org.getUser() != null && email.equals(org.getUser().getEmail())))
                    .toList();

            // Récupérer toutes les demandes de rôle pour ces conférences
            List<RoleRequest> roleRequests = organizedConferences.stream()
                    .flatMap(conference -> roleRequestService.findByConferenceIdAndStatus(conference.getId(), "PENDING").stream())
                    .toList();

            model.addAttribute("roleRequests", roleRequests);
            return "dynamic/notification/myNotification";
        } catch (Exception e) {
            logger.error("Error in myNotification", e);
            model.addAttribute("error", "An error occurred while loading notifications: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/myRoleRequests")
    public String myRoleRequests(Model model, Authentication authentication) {
        logger.debug("Accessing myRoleRequests with authentication: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to access myRoleRequests");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                logger.error("User not found for email: {}", email);
                model.addAttribute("error", "User not found.");
                return "error";
            }

            List<RoleRequest> roleRequests = roleRequestService.findByUserId(userOptional.get().getId());
            model.addAttribute("roleRequests", roleRequests);
            return "dynamic/role/myRoleRequests";
        } catch (Exception e) {
            logger.error("Error in myRoleRequests", e);
            model.addAttribute("error", "An error occurred while loading your role requests: " + e.getMessage());
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