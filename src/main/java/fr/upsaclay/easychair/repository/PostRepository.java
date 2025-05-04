package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByBodyIgnoreCase(String body);
}
