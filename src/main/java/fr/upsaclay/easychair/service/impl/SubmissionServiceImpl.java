package fr.upsaclay.easychair.service.impl;


import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.repository.AuthorRepository;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.repository.SubmissionRepository;
import fr.upsaclay.easychair.service.AuthorService;
//import fr.upsaclay.easychair.service.FileStorageService;
import fr.upsaclay.easychair.service.SubmissionService;
import fr.upsaclay.easychair.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;

    private final AuthorRepository authorRepository;

    private final UserService userService;


    @Override
    public Submission save(Submission submission) {
        return submissionRepository.save(submission);
    }

    @Override
    public Submission update(Submission submission) {
        return findOne(submission.getId()).map(existingSub ->{
            submission.setTitle(existingSub.getTitle());
            submission.setCreationDate(existingSub.getCreationDate());
            submission.setAbstractSub(existingSub.getAbstractSub());
            submission.setKeywords(existingSub.getKeywords());
            return submissionRepository.save(submission);
        }).orElseThrow(() -> new EntityNotFoundException("Submission introuvable avec l’ID : " + submission.getId()));
    }

    @Override
    public Optional<Submission> findOne(Long id) {
        return submissionRepository.findById(id);
    }

    @Override
    public List<Submission> findAll() {
        return submissionRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        submissionRepository.deleteById(id);
    }


    @Override
    public List<Submission> findByTitleIgnoreCase(String title) {
        if (title == null) {
            throw new IllegalArgumentException("titre  ne peut pas être null");
        }
        return submissionRepository.findByTitleIgnoreCase(title);
    }

    @Override
    public List<Submission> findSubmissionsByAuthor(User user) {
        List<Author> authors = authorRepository.findByUser(user);

        List<Submission> allSubmissions = new ArrayList<>();
        for (Author author : authors) {
            if (author.getSubmissions() != null) {
                allSubmissions.addAll(author.getSubmissions());
            }
        }
        return allSubmissions;
    }

    @Override
    public List <Submission> findSubmissionsByConference(Conference conference)
    {
        return submissionRepository.findByConference(conference);
    }


}