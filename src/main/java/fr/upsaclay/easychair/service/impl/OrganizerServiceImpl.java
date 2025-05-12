package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Organizer;
import fr.upsaclay.easychair.repository.OrganizerRepository;
import fr.upsaclay.easychair.service.OrganizerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrganizerServiceImpl implements OrganizerService {
    private final OrganizerRepository organizerRepository;

    @Override
    @Transactional
    public Organizer save(Organizer organizer) {
        Organizer savedOrganizer = organizerRepository.save(organizer);
        organizerRepository.flush(); // Forcer le flush pour garantir la persistance immédiate
        return savedOrganizer;
    }

    @Override
    public Organizer update(Organizer organizer) {
        return findOne(organizer.getId()).map(organizer1 -> {
            return organizerRepository.save(organizer1);
        }).orElseThrow(() -> new EntityNotFoundException("Organisateur introuvable avec l’ID : " + organizer.getId()));
    }

    @Override
    public Optional<Organizer> findOne(Long id) {
        return organizerRepository.findById(id);
    }

    @Override
    public List<Organizer> findAll() {
        return organizerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (organizerRepository.existsById(id)) {
            organizerRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Organizer with ID " + id + " does not exist.");
        }
    }
}
