package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMessageIgnoreCase(String message);
}
