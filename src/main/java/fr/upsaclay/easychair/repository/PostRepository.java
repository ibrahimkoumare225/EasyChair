package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
