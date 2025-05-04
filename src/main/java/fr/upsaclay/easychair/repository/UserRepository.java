package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrPseudoIgnoreCaseOrEmailIgnoreCase(String firstName,String lastName,String pseudo,String email);
}
