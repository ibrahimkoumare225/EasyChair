package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.repository.ConferenceRepository;
import fr.upsaclay.easychair.service.ConferenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;

    @Override
    public Conference save(Conference conference) {
        return conferenceRepository.save(conference);
    }

    @Override
    public Conference update(Conference conference) {
        return findOne(conference.getId()).map(existingConf ->{
            existingConf.setTitle(conference.getTitle());
            existingConf.setDescription(conference.getDescription());
            existingConf.setCreationDate(conference.getCreationDate());
            existingConf.setCommiteeAssignmentDate(conference.getCommiteeAssignmentDate());
            existingConf.setAbstractSubDate(conference.getAbstractSubDate());
            existingConf.setSubAssignmentDate(conference.getSubAssignmentDate());
            existingConf.setConcreteSubDate(conference.getConcreteSubDate());
            existingConf.setEvaluationDate(conference.getEvaluationDate());
            existingConf.setFinalSubDate(conference.getFinalSubDate());
            existingConf.setEndDate(conference.getEndDate());
            return conferenceRepository.save(existingConf);
        }).orElseThrow(() -> new EntityNotFoundException("Conference introuvable avec l’ID : " + conference.getId()));
    }

    @Override
    public Optional<Conference> findOne(Long id) {
        return conferenceRepository.findById(id);
    }

    @Override
    public List<Conference> findAll() {
        return conferenceRepository.findAll();
    }



    @Override
    public void deleteById(Long id) {
        conferenceRepository.deleteById(id);
    }

    @Override
    public List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description) {
        if (title == null || description == null) {
            throw new IllegalArgumentException("title ou description ne peuvent pas être null");
        }
        return conferenceRepository.findByTitleIgnoreCaseOrDescriptionIgnoreCase(title, description);
    }

    @Override

    public List<Conference> searchByTerm(String searchTerm) {
        if (searchTerm==null)
            throw new IllegalArgumentException("le terme  ne peut pas être null");
        return conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);

    }


}
