package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
