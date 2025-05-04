package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    Notification save(Notification notification);

    Notification update(Notification notification);

    Optional<Notification> findOne(Long id);

    List<Notification> findAll();

    void delete(Long id);

    List<Notification> findByMessageIgnoreCase(String message);

}
