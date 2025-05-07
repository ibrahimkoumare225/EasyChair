package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Alert;
import fr.upsaclay.easychair.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    // GET /alerts
    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertService.findAll();
    }

    // GET /alerts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Long id) {
        Optional<Alert> Alert = alertService.findOne(id);
        return Alert.isPresent() ? (ResponseEntity<Alert>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /alerts
    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.save(alert);
    }

    // PUT /alerts/{id}
    @PutMapping
    public Alert updateAlert(@RequestBody Alert alert) {
        return alertService.update(alert);
    }

    // DELETE /alerts/{id}
    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        alertService.delete(id);
    }

}
