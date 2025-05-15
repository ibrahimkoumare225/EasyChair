package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.controller.SubmissionController;
import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Organizer;
import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.model.enumates.Phase;
import fr.upsaclay.easychair.repository.AuthorRepository;
import fr.upsaclay.easychair.repository.ConferenceRepository;
import fr.upsaclay.easychair.repository.OrganizerRepository;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.service.ConferenceService;
import fr.upsaclay.easychair.service.MailSendingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class ConferenceServiceImpl implements ConferenceService {

    private  ConferenceRepository conferenceRepository;
    private  OrganizerRepository organizerRepository;
    private  ReviewerRepository reviewerRepository;

    private  AuthorRepository authorRepository;
    private  MailSendingService mailSendingService;

    private static final Logger logger = LoggerFactory.getLogger(ConferenceServiceImpl.class);

    ConferenceServiceImpl(ConferenceRepository conferenceRepository, OrganizerRepository organizerRepository,
                          AuthorRepository authorRepository,ReviewerRepository reviewerRepository){
        this.conferenceRepository = conferenceRepository;
        this.organizerRepository = organizerRepository;
        this.authorRepository = authorRepository;
        this.reviewerRepository = reviewerRepository;
    }
    @Autowired
    private void setMailSendingService(@Lazy MailSendingService mailSendingService){
        this.mailSendingService = mailSendingService;
    }


    @Override
    public List<Conference> findAll() {
        return conferenceRepository.findAllWithOrganizers();
    }

    @Override
    public List<Conference> findAllWithOrganizers() {
        return conferenceRepository.findAllWithOrganizers();
    }

    @Override
    public List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description, String keywords) {
        return conferenceRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrKeywordsContainingIgnoreCase(title, description, keywords);
    }

    @Override
    public Optional<Conference> findOne(Long id) {
        return conferenceRepository.findById(id);
    }

    @Override
    public Conference save(Conference conference) {
        return conferenceRepository.save(conference);
    }

    @Override
    public Conference update(Conference conference) {
        return findOne(conference.getId()).map(existingConf -> {
            existingConf.setTitle(conference.getTitle());
            existingConf.setDescription(conference.getDescription());
            existingConf.setPhase(conference.getPhase());
            // Préserver creationDate si la nouvelle valeur est null
            if (conference.getCreationDate() != null) {
                existingConf.setCreationDate(conference.getCreationDate());
            }
            //mettre à jout les keywords
            if(conference.getKeywords()!=null){
                existingConf.setKeywords(conference.getKeywords());
            }
            // Mettre à jour les autres dates seulement si non null
            if (conference.getCommiteeAssignmentDate() != null) {
                existingConf.setCommiteeAssignmentDate(conference.getCommiteeAssignmentDate());
            }
            if (conference.getAbstractSubDate() != null) {
                existingConf.setAbstractSubDate(conference.getAbstractSubDate());
            }
            if (conference.getSubAssignmentDate() != null) {
                existingConf.setSubAssignmentDate(conference.getSubAssignmentDate());
            }
            if (conference.getConcreteSubDate() != null) {
                existingConf.setConcreteSubDate(conference.getConcreteSubDate());
            }
            if (conference.getEvaluationDate() != null) {
                existingConf.setEvaluationDate(conference.getEvaluationDate());
            }
            if (conference.getFinalSubDate() != null) {
                existingConf.setFinalSubDate(conference.getFinalSubDate());
            }
            if (conference.getEndDate() != null) {
                existingConf.setEndDate(conference.getEndDate());
            }
            existingConf.setOnInvitation(conference.isOnInvitation());
            existingConf.setHiddenDescription(conference.isHiddenDescription());
            existingConf.setHiddenConf(conference.isHiddenConf());
            existingConf.setAnonymousReviewersToAuthors(conference.isAnonymousReviewersToAuthors());
            existingConf.setAnonymousReviewersToReviewers(conference.isAnonymousReviewersToReviewers());
            existingConf.setAnonymousAuthors(conference.isAnonymousAuthors());
            existingConf.setRestrictedAccessSubmission(conference.isRestrictedAccessSubmission());
            existingConf.setAssignmentByOrganizer(conference.isAssignmentByOrganizer());
            return conferenceRepository.save(existingConf);
        }).orElseThrow(() -> new EntityNotFoundException("Conference introuvable avec l’ID : " + conference.getId()));
    }

    @Override
    public void deleteById(Long id) {
        conferenceRepository.deleteById(id);
    }

    @Override
    public List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description) {
        return List.of();
    }

    @Override
    public List<Conference> findConferencesByUserEmail(String email) {
       List<Conference> organizerConferences=findConferencesByOrganizerEmail(email);
       List<Conference> reviewerConferences=findConferencesByReviewerEmail(email);
       List<Conference> authorConferences=findConferencesByAuthorEmail(email);
       // Combiner et supprimer les doublons
       Set<Conference> allConferences = new HashSet<>();
       allConferences.addAll(organizerConferences);
       allConferences.addAll(reviewerConferences);
       allConferences.addAll(authorConferences);

       return new ArrayList<>(allConferences);
    }

    @Override
    public List<Conference> findConferencesByOrganizerEmail(String email) {
           List<Conference> organizerConferences = organizerRepository.findByUserEmail(email)
                .stream()
                .map(Organizer::getConference)
                .toList();
           return new ArrayList<>(organizerConferences);

    }

    @Override
    public List<Conference> findConferencesByAuthorEmail(String email) {
        // Récupérer les conférences où l'utilisateur est Author
        List<Conference> authorConferences = authorRepository.findByUserEmail(email)
                .stream()
                .map(Author::getConference)
                .toList();
        return new ArrayList<>(authorConferences);
    }

    @Override
    public List<Conference> findConferencesByReviewerEmail(String email) {
        try {
            List<Reviewer> reviewers = reviewerRepository.findByUserEmail(email);
            return reviewers.stream()
                    .map(Reviewer::getConference)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des conférences pour le reviewer: " + email, e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isCreatingSubAvailable(Long conferenceID) {
        Optional<Conference> opt= findOne(conferenceID);
        if (opt.isPresent()) {
            Conference conf = opt.get();
            return conf.getPhase().equals(Phase.ABSTRACT_SUBMISSION);
        }
        else{
            throw new IllegalArgumentException("Bad ID");
        }
    }

    @Override
    public boolean isModifyingSubAvailable(Long conferenceID) {
        Optional<Conference> opt= findOne(conferenceID);
        if (opt.isPresent()) {
            Conference conf = opt.get();
            return conf.getPhase().equals(Phase.ABSTRACT_SUBMISSION)||
                    conf.getPhase().equals(Phase.CONCRETE_SUBMISSION)||
                    conf.getPhase().equals(Phase.FINAL_SUBMISSION);
        }
        else{
            throw new IllegalArgumentException("Bad ID");
        }
    }


    @Scheduled(cron="0 35 19 * * *")
    public void  checkPhase() {
        System.out.println("CHECK PHASE ");
        List<Conference> confToCheck = conferenceRepository.findConferencesByPhaseIsNot(Phase.CLOSED);
        LocalDate today = LocalDate.now();
        for (Conference conference : confToCheck) {
            switch (conference.getPhase()) {
                case INITIALIZATION -> {if (!today.isBefore(conference.getCommiteeAssignmentDate())) {
                    conference.setPhase(Phase.COMMITEE_ASSIGNMENT);
                    conference=update(conference);
                    mailSendingService.sendMailforPhase(conference.getId());

                }}
               case COMMITEE_ASSIGNMENT -> {if (!today.isBefore(conference.getAbstractSubDate())) {
                   conference.setPhase(Phase.ABSTRACT_SUBMISSION);
                   update(conference);
                   mailSendingService.sendMailforPhase(conference.getId());
               }}
               case ABSTRACT_SUBMISSION -> {if (!today.isBefore(conference.getSubAssignmentDate())) {
                    conference.setPhase(Phase.SUBMISSION_ASSIGNMENT);
                    update(conference);
                   mailSendingService.sendMailforPhase(conference.getId());
               }}
                case SUBMISSION_ASSIGNMENT -> {if (!today.isBefore(conference.getConcreteSubDate())) {
                    conference.setPhase(Phase.CONCRETE_SUBMISSION);
                    update(conference);
                    mailSendingService.sendMailforPhase(conference.getId());
                }}
                case CONCRETE_SUBMISSION -> {if (!today.isBefore(conference.getEvaluationDate())){
                    conference.setPhase(Phase.EVALUATION);
                    update(conference);
                    mailSendingService.sendMailforPhase(conference.getId());
                }}
                case EVALUATION -> {if (!today.isBefore(conference.getFinalSubDate())){
                    conference.setPhase(Phase.FINAL_SUBMISSION);
                    update(conference);
                    mailSendingService.sendMailforPhase(conference.getId());
                }}
                case FINAL_SUBMISSION -> {if (!today.isBefore(conference.getValidationDate())) {
                    conference.setPhase(Phase.VALIDATION);
                    update(conference);
                    mailSendingService.sendMailforPhase(conference.getId());
                }}
                case VALIDATION -> {if (!today.isBefore(conference.getEndDate())){
                    conference.setPhase(Phase.CLOSED);
                    update(conference);
                    mailSendingService.sendMailforPhase(conference.getId());
                }}
                default ->{}

            }

        }
    }

}
