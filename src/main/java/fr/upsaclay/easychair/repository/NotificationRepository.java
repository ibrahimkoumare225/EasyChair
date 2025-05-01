package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
