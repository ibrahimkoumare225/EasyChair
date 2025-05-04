package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Post;
import fr.upsaclay.easychair.repository.PostRepository;
import fr.upsaclay.easychair.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) {
        return findOne(post.getId()).map(existingPost->{
            existingPost.setBody(post.getBody());
            return  postRepository.save(existingPost);
        }).orElseThrow(() -> new EntityNotFoundException("Post introuvable avec l’ID : " + post.getId()));
    }

    @Override
    public Optional<Post> findOne(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> findByBodyIgnoreCase(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Body peut pas être null");
        }
        return postRepository.findByBodyIgnoreCase(body);
    }
}
