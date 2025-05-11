package fr.upsaclay.easychair.config;

import fr.upsaclay.easychair.model.Organizer;
import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.repository.OrganizerRepository;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.repository.AuthorRepository;
import fr.upsaclay.easychair.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;
    private final OrganizerRepository organizerRepository;
    private final ReviewerRepository reviewerRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository,
                                    OrganizerRepository organizerRepository,
                                    ReviewerRepository reviewerRepository,
                                    AuthorRepository authorRepository) {
        this.userRepository = userRepository;
        this.organizerRepository = organizerRepository;
        this.reviewerRepository = reviewerRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Loading user by email: {}", email);
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            logger.debug("Found user: {}", user.getEmail());

            // Charger les rôles de l'utilisateur
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            // Vérifier Organizer
            List<Organizer> organizers = organizerRepository.findByUserId(user.getId());
            if (!organizers.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ORGANIZER"));
                logger.debug("User {} has ROLE_ORGANIZER, found {} organizers", email, organizers.size());
            } else {
                logger.debug("User {} has no ROLE_ORGANIZER", email);
            }

            // Vérifier Reviewer
            List<Reviewer> reviewers = reviewerRepository.findByUserId(user.getId());
            if (!reviewers.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_REVIEWER"));
                logger.debug("User {} has ROLE_REVIEWER, found {} reviewers", email, reviewers.size());
            }

            // Vérifier Author
            List<Author> authors = authorRepository.findByUserId(user.getId());
            if (!authors.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR"));
                logger.debug("User {} has ROLE_AUTHOR, found {} authors", email, authors.size());
            }

            // Ajouter ROLE_USER par défaut si aucun rôle spécifique
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                logger.debug("User {} has default ROLE_USER", email);
            }

            logger.debug("Authorities for user {}: {}", email, authorities);
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.isEnabled(),
                    true, // accountNonExpired
                    true, // credentialsNonExpired
                    true, // accountNonLocked
                    authorities
            );
        } catch (Exception e) {
            logger.error("Error loading user {}", email, e);
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage(), e);
        }
    }
}
