package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Notification;
import fr.upsaclay.easychair.repository.NotificationRepository;
import fr.upsaclay.easychair.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        return findOne(notification.getId()).map(existingNotif->{
            notification.setMessage(existingNotif.getMessage());
            notification.setSendingDate(existingNotif.getSendingDate());
            return notificationRepository.save(existingNotif);
        }).orElseThrow(() -> new EntityNotFoundException("Notification introuvable avec l’ID : " + notification.getId()));
    }

    @Override
    public Optional<Notification> findOne(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> findByMessageIgnoreCase(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message ne peux pas être null");
        }
        return notificationRepository.findByMessageIgnoreCase(message);
    }
}
