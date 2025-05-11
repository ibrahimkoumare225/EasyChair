package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Organizer;
import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.repository.AuthorRepository;
import fr.upsaclay.easychair.repository.ConferenceRepository;
import fr.upsaclay.easychair.repository.OrganizerRepository;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.service.ConferenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final OrganizerRepository organizerRepository;
    private final ReviewerRepository reviewerRepository;
    private final AuthorRepository authorRepository;

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
    public List<Conference> findConferencesByUserEmail(String email) {
        // Récupérer les conférences où l'utilisateur est Organizer
        List<Conference> organizerConferences = organizerRepository.findByUserEmail(email)
                .stream()
                .map(Organizer::getConference)
                .toList();

        // Récupérer les conférences où l'utilisateur est Reviewer
        List<Conference> reviewerConferences = reviewerRepository.findByUserEmail(email)
                .stream()
                .map(Reviewer::getConference)
                .toList();

        // Récupérer les conférences où l'utilisateur est Author
        List<Conference> authorConferences = authorRepository.findByUserEmail(email)
                .stream()
                .map(Author::getConference)
                .toList();

        // Combiner et supprimer les doublons
        Set<Conference> allConferences = new HashSet<>();
        allConferences.addAll(organizerConferences);
        allConferences.addAll(reviewerConferences);
        allConferences.addAll(authorConferences);

        return new ArrayList<>(allConferences);
    }
}