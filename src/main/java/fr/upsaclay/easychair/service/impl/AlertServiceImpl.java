package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Alert;
import fr.upsaclay.easychair.repository.AlertRepository;
import fr.upsaclay.easychair.service.AlertService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AlertServiceImpl implements AlertService {
    private final AlertRepository alertRepository;
    @Override
    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }

    @Override
    public Alert update(Alert alert) {
        return findOne(alert.getId()).map(existingAlert-> {
            existingAlert.setBody(alert.getBody());
            return alertRepository.save(existingAlert);
        }).orElseThrow(() -> new EntityNotFoundException("Alert introuvable avec l’ID : " + alert.getId()));
    }

    @Override
    public Optional<Alert> findOne(Long id) {
        return alertRepository.findById(id);
    }

    @Override
    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        alertRepository.deleteById(id);
    }

    @Override
    public List<Alert> findByBodyIgnoreCase(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Body peut pas être null");
        }
        return alertRepository.findByBodyIgnoreCase(body);
    }
}
