/*
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Post;
import fr.upsaclay.easychair.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class PostController {


    private final PostService postService;

    // GET /posts
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    // GET /posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.findOne(id);
        return post.isPresent() ? (ResponseEntity<Post>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /posts
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.save(post);
    }

    // PUT /posts/{id}
    @PutMapping
    public Post updatePost(@RequestBody Post post) {
        return postService.update(post);
    }

    // DELETE /posts/{id}
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.delete(id);
    }

    //GET /users/search
    @GetMapping("/search")
    public List<Post>getPostsByBodyIgnoreCase(@RequestBody String body) {
        return postService.findByBodyIgnoreCase(body);
    }


}
*/
