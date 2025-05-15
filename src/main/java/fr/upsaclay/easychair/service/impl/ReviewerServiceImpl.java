package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.service.ReviewerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewerServiceImpl implements ReviewerService {
    private final ReviewerRepository reviewerRepository;
    @Override
    public Reviewer save(Reviewer reviewer) {
        return reviewerRepository.save(reviewer);
    }

    @Override
    public Reviewer update(Reviewer reviewer) {
        return null;
    }

    @Override
    public Optional<Reviewer> findOne(Long id) {
        return reviewerRepository.findById(id);
    }

    @Override
    public List<Reviewer> findAll() {
        return reviewerRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        reviewerRepository.deleteById(id);
    }

    @Override
    public Optional<Reviewer> findByUserId(Long userId) {
        return reviewerRepository.findById(userId);
    }

    @Override
    public List<Reviewer> findByConferenceID(Long conferenceId) {
        return reviewerRepository.findByConferenceId(conferenceId);
    }

}
