
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
import java.util.Date;
import java.util.List;
import java.util.Optional;


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

    @GetMapping("/user/{userId}")
    public String showUserSubmissions( Model model,Authentication authentication) {

        if (authentication==null||!authentication.isAuthenticated()){
            return "redirect:/login";
        }
        logger.debug("Access to Submissions of user {}", authentication.getName());

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
    public String showDetailSubmission(Model model, @PathVariable Long id,Authentication authentication) {

        if (authentication==null||!authentication.isAuthenticated()){
            return "redirect:/login";
        }
        logger.debug("Access to Submissions of user {}", authentication.getName());
        Optional<Submission> submission = submissionService.findOne(id);
        if (submission.isPresent()) {
            //Verif user est dans la conference
            List<Conference> conferences = conferenceService.findConferencesByUserEmail(authentication.getName());
            if (conferences.contains(submission.get().getConference())){
                model.addAttribute("submission", submission.get());
                return "dynamic/submission/detailSubmission";
            }
        }
        return "redirect:/home";
    }


    @GetMapping("/ajouterSubmission")
    public String showAddSubForm(@RequestParam Long conferenceId, Model model, Authentication authentication) {
        //Ajout de la conference pour rattachement de la submission
        if (authentication==null||!authentication.isAuthenticated()){
            logger.warn("Unauthenticated attempt to showAddSubForm");
            return "redirect:/login";
        }
        try {
            Optional<Conference> conference = conferenceService.findOne(conferenceId);
            if (conference.isPresent()) {
                logger.debug("Founded conference ID :{}", conference.get().getId());
                List<Conference> conferences = conferenceService.findConferencesByAuthorEmail(authentication.getName());
                Optional<Author> matchedAuthor =  conferences.stream()
                        .flatMap(c -> c.getAuthors().stream())
                        .filter(author -> author.getUser().getEmail().equals(authentication.getName()))
                        .findFirst();
                logger.debug(" Matching conference  with Author");
                if (matchedAuthor.isEmpty()){
                    logger.warn("matchedAuthor is empty");
                    return "redirect:/conference";
                }
                if(matchedAuthor.get().getConference().equals(conference.get())) {
                    logger.debug("matched conference");
                    //Doit etre set dans l'autre sens dans save
                    Submission submission = new Submission();
                    submission.setConference(conference.get());
                    submission.getAuthors().add(matchedAuthor.get());
                    model.addAttribute("submission", submission);
                    return "dynamic/submission/submissionForm";
                } else {
                    logger.warn("No match with of conference {} for Author with user {}", conference.get().getId(), authentication.getName());
                    return "redirect:/conference";
                }

            } else {
                logger.warn("No conference with ID {}", conferenceId);
                return "redirect:/conference";
            }
        }catch (Exception e){
        logger.error("error in showAddSubForm", e);}
        return "redirect:/conference";
    }


    @GetMapping("/modifierSubmission")
    public String showModifySubForm(@RequestParam Long submissionId,  Model model,Authentication authentication) {

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
            model.addAttribute(submission.get());
            return "dynamic/submission/submissionForm";
        } catch (Exception e) {
            logger.error("error in showModifySub",e);
            return "redirect:/conference";
        }
    }




    @PostMapping("/save")
    @Transactional
    public String saveSubmission(@ModelAttribute Submission submission,
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
        logger.debug("Checking user have  author {} and  Conference {}",authorID,conferenceId);

        Optional<Role> matchedAuthor =userOptional.get().getRoles().stream()
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
        evaluation=evaluationService.save(evaluation);
        logger.debug("creating empty Evaluation {} for submission ",evaluation.getId());
        submission.setEvaluation(evaluation);
        submission=submissionService.save(submission);
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
        redirectAttributes.addAttribute("id",submission.getId());

        return "redirect:/conference";//"redirect:/submissions/submissionDetail{id}";
    }




    // GET /submissions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        Optional<Submission> submission = submissionService.findOne(id);
        return submission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    // PUT /submissions/{id}
    @PostMapping("/update")
    public String updateSubmission(@ModelAttribute Submission submission,Authentication authentication,
                                       RedirectAttributes redirectAttributes) {logger.debug("Saving new submission: {}", submission);

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
        logger.debug("Checking user have  author {} and  Conference {}",authorID,conferenceId);

        Optional<Role> matchedAuthor =userOptional.get().getRoles().stream()
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
            return"redirect:/conference";
        }
        submissionToUpdate.get().setKeywords(submission.getKeywords());
        if (submissionToUpdate.get().getConference().getPhase().equals(Phase.ABSTRACT_SUBMISSION)){
            submissionToUpdate.get().setAbstractSub(submission.getAbstractSub());
        }
        if (submissionToUpdate.get().getConference().getPhase().equals(Phase.CONCRETE_SUBMISSION)){
            submissionToUpdate.get().setSubFiles(submission.getSubFiles());
        }
        submissionService.update(submissionToUpdate.get());
        return "redirect:/conference";

    }

    // DELETE /submissions/{id}
    @DeleteMapping("/{id}")
    public void deleteSubmission(@PathVariable Long id) {
        submissionService.delete(id);
    }

    // GET /submissions/search
    @GetMapping("/search")
    public List<Submission> searchByTitle(@RequestBody String title) {
        return submissionService.findByTitleIgnoreCase(title);
    }
}
