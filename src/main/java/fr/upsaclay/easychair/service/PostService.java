package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post save(Post post);

    Post update(Post post);

    Optional<Post> finOne(Long id);

    List<Post> findAll();

    void delete(Long id);

    List<Post> findByBodyIgnoreCase(String body);

}
