
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.model.Post;
import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.service.EvaluationService;
import fr.upsaclay.easychair.service.PostService;
import fr.upsaclay.easychair.service.ReviewerService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/posts")
public class PostController {


    private final PostService postService;
    private final EvaluationService evaluationService;
    private final ReviewerService reviewerService;
    private final UserService userService;

    // GET /form/ajouterPost/{id}
    @GetMapping("/form/ajouterPost/{id_evaluation}")
    public String showPostsEvaluation(@PathVariable Long id_evaluation, Model model) {
        Optional<Evaluation> evaluation = evaluationService.findOne(id_evaluation);
        if(evaluation.isPresent()) {
            model.addAttribute("evaluation", evaluation.get());
            model.addAttribute("post", new Post());
            return "dynamic/evaluation/evaluationThread";
        }else
            return "error/404";
    }

    @GetMapping("/form/createPost/{id_evaluation}")
    public String showAddPostForm(@PathVariable Long id_evaluation, Model model) {

        Optional<Evaluation> evaluation = evaluationService.findOne(id_evaluation);
        if (evaluation.isEmpty()) {
            return "error/404";
        }

        Post post = new Post();
        post.setEvaluation(evaluation.get());

        model.addAttribute("post", post);
        return "dynamic/post/postForm";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Post post) {
        // Ajouter la date actuelle
        post.setDate(LocalDateTime.now());

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOpt = userService.findByEmail(email);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        User user = userOpt.get();

        // Trouver le reviewer associé à cet utilisateur
        Reviewer reviewer = reviewerService.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        post.setReviewer(reviewer);
        postService.save(post);

        return "redirect:/form/ajouterPost/" + post.getEvaluation().getId();
    }

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
