package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByBodyIgnoreCase(String body);
}
