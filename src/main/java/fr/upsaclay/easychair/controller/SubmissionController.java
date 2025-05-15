
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.model.enumates.Phase;
import fr.upsaclay.easychair.model.enumates.SubType;
import fr.upsaclay.easychair.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Controller
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final UserService userService;
    private final ConferenceService conferenceService;
    private static final Logger logger = LoggerFactory.getLogger(SubmissionController.class);
    private final AuthorService authorService;
    private final EvaluationService evaluationService;
    private final FileStorageService fileStorageService;

    @GetMapping("/user")
    public String showUserSubmissions(Model model, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        Optional<User> user = userService.findByEmail(authentication.getName());
        if (user.isPresent()) {
            List<Submission> submissions = submissionService.findSubmissionsByAuthor(user.get());
            model.addAttribute("submissions", submissions);
            model.addAttribute("user", user.get());
            return "/dynamic/submission/listSubmission"; // Nom de la vue Thymeleaf
        } else {
            return "redirect:/conference";
        }
    }

    @GetMapping("/submissionDetail/{id}")
    public String showDetailSubmission(Model model, @PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<Submission> submission = submissionService.findOne(id);
        if (submission.isPresent()) {
            logger.debug("Founded submission ID :{}", submission.get().getId());
            //Verif user est dans la conference
//            List<Conference> conferences = conferenceService.findConferencesByUserEmail(authentication.getName());
//            if (conferences.contains(submission.get().getConference())) {
            model.addAttribute("submission", submission.get());
            model.addAttribute("files", fileStorageService.listFiles(submission.get().getId().toString()));
            return "dynamic/submission/detailSubmission";
//
        } else {
            return "redirect:/conference";
        }
    }

    @GetMapping("/ajouterSubmission")
    public String showAddSubForm(@RequestParam Long conferenceId, Model model, Authentication authentication) {
        //Ajout de la conference pour rattachement de la submission
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to showAddSubForm");
            return "redirect:/login";
        }
        try {
            Optional<Conference> conference = conferenceService.findOne(conferenceId);
            if (conference.isPresent()) {
                logger.debug("Founded conference ID :{}", conference.get().getId());
                Optional<Author> matchedAuthor = conference.get().getAuthors().stream()
                        .filter(author -> author.getUser().getEmail().equals(authentication.getName()))
                        .findFirst();
                logger.debug(" Matching conference  with Author");
                if (matchedAuthor.isEmpty()) {
                    logger.warn("matchedAuthor is empty");
                    return "redirect:/conference";
                }
                if (matchedAuthor.get().getConference().equals(conference.get())) {
                    logger.debug("matched conference");
                    //Doit etre set dans l'autre sens dans save
                    Submission submission = new Submission();
                    submission.setConference(conference.get());
                    submission.getAuthors().add(matchedAuthor.get());
                    model.addAttribute("submission", submission);
                    return "dynamic/submission/submissionForm";
                } else {
                    logger.warn("No match with  conference {} for Author {} : has conference {} ", conference.get().getId(),
                            matchedAuthor.get().getId(),matchedAuthor.get().getConference().getId(), authentication.getName());
                    return "redirect:/conference";
                }

            } else {
                logger.warn("No conference with ID {}", conferenceId);
                return "redirect:/conference";
            }
        } catch (Exception e) {
            logger.error("error in showAddSubForm", e);
        }
        return "redirect:/conference";
    }


    @GetMapping("/modifierSubmission")
    public String showModifySubForm(@RequestParam Long submissionId, Model model, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to showModifySubForm");
            return "redirect:/login";
        }
        try {
            Optional<Submission> submission = submissionService.findOne(submissionId);
            if (submission.isEmpty()) {
                logger.warn("Submission {} not found in database", submissionId);
                return "redirect:/conference";
            }

            logger.debug("founded Submission   {}", submissionId);
            Optional<User> user = userService.findByEmail(authentication.getName());
            if (user.isEmpty()) {
                logger.warn("user {} not found in database", authentication.getName());
                return "redirect:/conference";
            }
            //@TODO a revoir si plusieurs auteurs
            logger.debug("Matching user {} with submission {}", authentication.getName(), submissionId);
            Optional<Role> matchedAuthor = user.get().getRoles().stream()
                    .filter(role -> role.getId().equals(submission.get().getAuthors().get(0).getId())
                            && role.getConference().getId().equals(submission.get().getConference().getId()))
                    .findFirst();
            if (matchedAuthor.isEmpty()) {
                logger.warn("No match with  user {} as Author of submission{}", authentication.getName(), submissionId);
                return "redirect:/conference";
            }
            model.addAttribute("submission", submission.get());
            model.addAttribute("files", fileStorageService.listFiles(submission.get().getId().toString()));
            return "dynamic/submission/submissionForm";
        } catch (Exception e) {
            logger.error("error in showModifySub", e);
            return "redirect:/conference";
        }
    }

    @PostMapping("/save")
    @Transactional
    public String saveSubmission(@ModelAttribute Submission submission,
                                 @RequestParam("keywordList") String keywordList,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        logger.debug("Saving new submission: {}", submission);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to save conference");
            return "redirect:/login";
        }

        String email = authentication.getName();
        logger.debug("Authenticated user email for saveSubmission: {}", email);
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            logger.error("User not found for email: {}", email);
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/conference";
        }
        Long authorID = submission.getAuthors().get(0).getId();
        Long conferenceId = submission.getConference().getId();
        logger.debug("Checking user have  author {} and  Conference {}", authorID, conferenceId);

        Optional<Role> matchedAuthor = userOptional.get().getRoles().stream()
                .filter(role -> role.getId().equals(authorID)
                        && role.getConference().getId().equals(conferenceId))
                .findFirst();
        if (matchedAuthor.isEmpty()) {
            logger.warn("wrong Role Id , expected {}", authorID);
            redirectAttributes.addFlashAttribute("error", "Wrong Role Id.");
            return "redirect:/conference";
        }
        //setConference & setAuthor done in showModifyForm
        submission.setCreationDate(Date.from(Instant.now()));
        submission.setStatus(SubType.PROGRESS);
        logger.debug("Setting creationDate and Status for {}", submission.getId());
        Evaluation evaluation = new Evaluation();
        evaluation.setSubmission(submission);
        evaluation = evaluationService.save(evaluation);
        logger.debug("creating empty Evaluation {} for submission ", evaluation.getId());
        submission.setEvaluation(evaluation);
        //Permet de convertir le string en List pour le set dans submission
        List<String> keywords = Arrays.stream(keywordList.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        submission.setKeywords(keywords);
        submission = submissionService.save(submission);
        logger.debug("Saved new submission: {}", submission.toString());
        Optional<Conference> conference = conferenceService.findOne(submission.getConference().getId());
        if (conference.isPresent()) {
            conference.get().getSubmissions().add(submission);
            conferenceService.update(conference.get());
            logger.debug("conference {}linked with new submission{}", conference.get().getId(), submission.getId());


        } else {
            //Rollback
            logger.warn("Conference {} not found in database", submission.getConference().getId());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.debug("Rollback for {}", submission.getId());
            redirectAttributes.addFlashAttribute("error", "Conference not found.");
            return "redirect:/conference";
        }
        //@TODO qu'un author géré
        Optional<Author> author = authorService.findOne(submission.getAuthors().get(0).getId());
        if (author.isPresent()) {
            author.get().getSubmissions().add(submission);
            authorService.update(author.get());
            logger.debug("author {}linked with new submission{}", author.get().getId(), submission.getId());
        } else {
            logger.warn("Author {} not found in database", submission.getAuthors().get(0).getId());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.debug("Rollback for {}", submission.getId());
            redirectAttributes.addFlashAttribute("error", "Author not found.");
            return "redirect:/conference";
        }
        redirectAttributes.addAttribute("id", submission.getId());

        return "redirect:/conference";//"redirect:/submissions/submissionDetail{id}";
    }

    @GetMapping("/conference/{id_conference}")
    public String showSubmissionConference(@PathVariable Long id_conference, Model model) {
        Optional<Conference> conference = conferenceService.findOne(id_conference);
        if (conference.isPresent()) {
            Long id_user = conference.get().getOrganizers().get(0).getId();
            Optional<User> user = userService.findOne(id_user);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
            }
            List<Submission> submissions = submissionService.findSubmissionsByConference(conference.get());
            model.addAttribute("submissions", submissions); // Uniformiser le nom de l'attribut (submissions au lieu de submission)
            return "dynamic/submission/listSubmission";
        } else {
            return "error/404";
        }
    }

    // GET /submissions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        Optional<Submission> submission = submissionService.findOne(id);
        return submission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // PUT /submissions/{id}
    @PostMapping("/update")
    public String updateSubmission(@ModelAttribute Submission submission, Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        logger.debug("Saving new submission: {}", submission);

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to save conference");
            return "redirect:/login";
        }

        String email = authentication.getName();
        logger.debug("Authenticated user email for saveSubmission: {}", email);
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            logger.error("User not found for email: {}", email);
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/conference";
        }
        Long authorID = submission.getAuthors().get(0).getId();
        Long conferenceId = submission.getConference().getId();
        logger.debug("Checking user have  author {} and  Conference {}", authorID, conferenceId);

        Optional<Role> matchedAuthor = userOptional.get().getRoles().stream()
                .filter(role -> role.getId().equals(authorID)
                        && role.getConference().getId().equals(conferenceId))
                .findFirst();
        if (matchedAuthor.isEmpty()) {
            logger.warn("wrong Role Id , expected {}", authorID);
            redirectAttributes.addFlashAttribute("error", "Wrong Role Id.");
            return "redirect:/conference";
        }
        Optional<Submission> submissionToUpdate = submissionService.findOne(submission.getId());
        if (submissionToUpdate.isEmpty()) {
            logger.warn("submission {} not found in database", submission.getId());
            return "redirect:/conference";
        }
        submissionToUpdate.get().setKeywords(submission.getKeywords());
        if (submissionToUpdate.get().getConference().getPhase().equals(Phase.ABSTRACT_SUBMISSION)) {
            submissionToUpdate.get().setAbstractSub(submission.getAbstractSub());
        }
        if (submissionToUpdate.get().getConference().getPhase().equals(Phase.CONCRETE_SUBMISSION)) {
            submissionToUpdate.get().setSubFiles(submission.getSubFiles());
        }
        submissionService.update(submissionToUpdate.get());

        return "redirect:/conference";

    }

    // DELETE /submissions/{id}
    @PostMapping("/delete")
    public String deleteSubmission(@RequestParam Long submissionId, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated attempt to showModifySubForm");
            return "redirect:/login";
        }
        try {
            Optional<Submission> submission = submissionService.findOne(submissionId);
            if (submission.isEmpty()) {
                logger.warn("Submission {} not found in database", submissionId);
                redirectAttributes.addFlashAttribute("error", "Submission not found.");
                return "redirect:/conference";
            }

            logger.debug("founded Submission   {}", submissionId);
            Optional<User> user = userService.findByEmail(authentication.getName());
            if (user.isEmpty()) {
                logger.warn("user {} not found in database", authentication.getName());
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/conference";
            }
            //@TODO a revoir si plusieurs auteurs
            logger.debug("Matching user {} with submission {}", authentication.getName(), submissionId);
            Optional<Role> matchedAuthor = user.get().getRoles().stream()
                    .filter(role -> role.getId().equals(submission.get().getAuthors().get(0).getId())
                            && role.getConference().getId().equals(submission.get().getConference().getId()))
                    .findFirst();
            if (matchedAuthor.isEmpty()) {
                logger.warn("No match with  user {} as Author of submission{}", authentication.getName(), submissionId);
                redirectAttributes.addFlashAttribute("error", "You are not author of this submission.");
                return "redirect:/conference";
            }

            Optional<Author> author=authorService.findOne(submission.get().getAuthors().get(0).getId());
            if (author.isEmpty()) {
                logger.warn("author not found for submission {}",submissionId);
                redirectAttributes.addFlashAttribute("error", "Author not found.");
                return "redirect:/conference";
            }
            author.get().getSubmissions().removeIf(sub -> sub.getId().equals(submissionId));
            authorService.update(author.get());

            Optional<Conference> conference = conferenceService.findOne(submission.get().getConference().getId());
            if (conference.isEmpty()) {
                logger.warn("Conference not found for  submission {}",submissionId);
                redirectAttributes.addFlashAttribute("error", "Conference not found.");
                return "redirect:/conference";
            }
            conference.get().getSubmissions().removeIf(sub -> sub.getId().equals(submissionId));
            conferenceService.update(conference.get());
            submissionService.delete(submissionId);
            logger.debug("submission {} deleted", submissionId);
            redirectAttributes.addFlashAttribute("message", "Submission deleted.");
            return "redirect:/conference";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error in Deletion");
            return "redirect:/conference";
        }
    }

    // GET /submissions/search
    @GetMapping("/search")
    public List<Submission> searchByTitle(@RequestBody String title) {
        return submissionService.findByTitleIgnoreCase(title);
    }
}

