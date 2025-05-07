package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Alert;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.enumates.AlertType;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.repository.AlertRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceImplTest {
    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertServiceImpl alertService;
    private Alert alert;
    private Alert alert2;
    private List<Alert> alertList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test


        alert = new Alert();
        alert.setId(1L);
        alert.setBody("This is a sample alert");
        alert.setType(AlertType.CONTENT_REPORT);

        alert2 = new Alert();
        alert2.setId(2L);
        alert.setBody("This is a sample alert");
        alert.setType(AlertType.CONFLICT_OF_INTEREST);

        alertList = Arrays.asList(alert, alert2);

    }

    @Test
    void save_ShouldReturnSavedAlert() {
        Alert newAlert = new Alert();
        newAlert.setBody(alert.getBody());
        newAlert.setType(alert.getType());
        // Given

        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation ->{
            Alert alert = invocation.getArgument(0);
            alert.setId(1L);
            return alert;
        });

        // When
        Alert savedAlert = alertService.save(alert);

        // Then
        assertNotNull(savedAlert);
        assertSame(alert,savedAlert);
        assertEquals(1L,savedAlert.getId());
        verify(alertRepository, times(1)).save(alert);
    }

    @Test
    void update_WhenAlertExists_ShouldReturnUpdatedAlert() {
        // Given


        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> {
            Alert alert = invocation.getArgument(0);
            alert.setBody("This is a new body");
            return alert;
        });

        // When
        Alert updatedAlert = alertService.update(alert);

        // Then
        assertNotNull(updatedAlert);
        assertEquals(1L, updatedAlert.getId());
        assertEquals("This is a new body", updatedAlert.getBody());
        verify(alertRepository, times(1)).findById(1L);
        verify(alertRepository, times(1)).save(alert);
    }

    @Test
    void update_WhenAlertDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Given
        when(alertRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> alertService.update(alert));
        verify(alertRepository, times(1)).findById(alert.getId());
        verify(alertRepository, never()).save(any(Alert.class));
    }
}

