/*
package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.repository.ConferenceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConferenceServiceImplTest {
    @Mock
    private ConferenceRepository conferenceRepository;

    @InjectMocks
    private ConferenceServiceImpl conferenceService;
    private Conference conference;
    private Conference conference2;
    private List<Conference> conferenceList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test


        conference = new Conference();
        conference.setId(1L);
        conference.setTitle("Sample Conference");
        conference.setDescription("This is a sample conference description.");


        conference2 = new Conference();
        conference2.setId(2L);
        conference2.setTitle("Sample Conference2");
        conferenceList = Arrays.asList(conference, conference2);

    }
}
*/
