/*
//package fr.upsaclay.easychair.repository;
//
//import fr.upsaclay.easychair.model.Conference;
//import fr.upsaclay.easychair.service.ConferenceService;
//import fr.upsaclay.easychair.service.DataInitializer;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//
//class ConferenceRepositoryTest {
//
//    @Autowired
//    private ConferenceRepository conferenceRepository;
//    @Autowired
//    private ConferenceService conferenceService;
//
//
//    @Test
//    public void testSearchByTerm_NoMatch() {
//        // Test quand aucun résultat ne correspond au terme recherché
//        String searchTerm = "TermeInexistant";
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        // Vérification qu'aucune conférence n'est trouvée
//        assertTrue(result.isEmpty(), "Aucune conférence ne devrait être trouvée avec le terme: " + searchTerm);
//    }
//
//    @Test
//    public void testSearchByTerm_MatchTitle() {
//        // Test quand le terme correspond au titre uniquement
//        String searchTerm = "Sample";  // Doit correspondre à "Sample Conference" dans le titre
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        // Vérification qu'une conférence est trouvée et que son titre contient le terme recherché
//        assertFalse(result.isEmpty(), "Une conférence devrait être trouvée avec le terme dans le titre: " + searchTerm);
//        assertTrue(result.get(0).getTitle().toLowerCase().contains(searchTerm.toLowerCase()),
//                "Le titre de la conférence devrait contenir: " + searchTerm);
//    }
//
//    @Test
//    public void testSearchByTerm_MatchDescription() {
//        // Test quand le terme correspond à la description uniquement
//        String searchTerm = "sample";  // Doit correspondre à "This is a sample conference description"
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        // Vérification qu'une conférence est trouvée et que sa description contient le terme recherché
//        assertFalse(result.isEmpty(), "Une conférence devrait être trouvée avec le terme dans la description: " + searchTerm);
//        assertTrue(result.get(0).getDescription().toLowerCase().contains(searchTerm.toLowerCase()),
//                "La description de la conférence devrait contenir: " + searchTerm);
//    }
//
//    @Test
//    public void testSearchByTerm_MatchDescriptionWithAPartofArg() {
//        // Test quand le terme correspond à la description uniquement
//        String searchTerm = "sample Description";  // Doit correspondre à "This is a sample conference description"
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        // Vérification qu'une conférence est trouvée et que sa description contient le terme recherché
//        assertFalse(result.isEmpty(), "Une conférence devrait être trouvée avec le terme dans la description: " + searchTerm);
//        assertTrue(result.get(0).getDescription().toLowerCase().contains(searchTerm.toLowerCase()),
//                "La description de la conférence devrait contenir: " + searchTerm);
//    }
//
//    @Test
//    public void testSearchByTerm_MatchKeyword() {
//        // Test quand le terme correspond à un keyword
//        String searchTerm = "test";  // Un des mots-clés ajoutés à la conférence
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        // Vérification qu'une conférence est trouvée avec le mot-clé recherché
//        assertFalse(result.isEmpty(), "Une conférence devrait être trouvée avec le mot-clé: " + searchTerm);
//
//        // Vérification que la conférence contient bien le mot-clé recherché
//        Conference conference = result.get(0);
//        boolean containsKeyword = conference.getKeywords().stream()
//                .anyMatch(keyword -> keyword.toLowerCase().contains(searchTerm.toLowerCase()));
//
//        assertTrue(containsKeyword, "La conférence devrait contenir le mot-clé: " + searchTerm);
//    }
//
//    @Test
//    public void testSearchByTerm_MatchMultipleFields() {
//        // Test quand le terme correspond à plusieurs champs (par exemple "conférence" pourrait être
//        // dans le titre et la description)
//        String searchTerm = "Conference";  // Présent dans le titre et potentiellement dans la description
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        // Vérification qu'au moins une conférence est trouvée
//        assertFalse(result.isEmpty(), "Une conférence devrait être trouvée avec le terme: " + searchTerm);
//
//        // Vérification qu'on n'a pas de doublons (même si le terme est présent dans plusieurs champs)
//        assertEquals(1, result.size(), "Il ne devrait y avoir qu'une seule conférence distincte dans les résultats");
//    }
//
//    @Test
//    public void testSearchByTerm_CaseInsensitivity() {
//        // Test de l'insensibilité à la casse
//        String searchTerm = "sample";  // "Sample" en minuscules
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        assertFalse(result.isEmpty(), "Une recherche insensible à la casse devrait trouver des résultats");
//    }
//
//    @Test
//    public void testSearchByTerm_PartialMatch() {
//        // Test de correspondance partielle
//        String searchTerm = "Samp";  // Juste une partie de "Sample"
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords(searchTerm);
//
//        assertFalse(result.isEmpty(), "Une recherche avec correspondance partielle devrait trouver des résultats");
//    }
//
//    @Test void testSearchByTerm_HiddenConf(){
//        String searchTerm = "test";
//        Optional<Conference> conf =conferenceService.findOne(1L);
//        if (conf.isEmpty())
//            throw new IllegalArgumentException("Mauvais id");
//        conf.get().setHiddenConf(true);
//        conferenceRepository.save(conf.get());
//        List<Conference> result = conferenceRepository.searchByTermInTitleOrDescriptionOrKeywords("test");
//        assertTrue(result.isEmpty());
//
//    }
//
//}
//
*/
