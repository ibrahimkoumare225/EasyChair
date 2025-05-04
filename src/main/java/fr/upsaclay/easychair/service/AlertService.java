package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Alert;

import java.util.List;
import java.util.Optional;

public interface AlertService {

    Alert save(Alert alert);

    Alert update(Alert alert);

    Optional<Alert> findOne(Long id);

    List<Alert> findAll();
    void delete(Long id);

    List<Alert> findByBodyIgnoreCase(String body);

}
