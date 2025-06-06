/*
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Notification;
import fr.upsaclay.easychair.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    // GET /notifications
    @GetMapping("/homeNotification")
    public List<Notification> getAllNotifications() {
        return notificationService.findAll();
    }

    // GET /notifications/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.findOne(id);
        return notification.isPresent() ? (ResponseEntity<Notification>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /notifications
    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }

    // PUT /notifications/{id}
    @PutMapping
    public Notification updateNotification(@RequestBody Notification notification) {
        return notificationService.update(notification);
    }

    // DELETE /notifications/{id}
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.delete(id);
    }

    //GET /notifications/search
    @GetMapping("/search")
    public List<Notification> getByMessageIgnoreCase(@RequestParam String message) {
        return notificationService.findByMessageIgnoreCase(message);
    }

}
*/
