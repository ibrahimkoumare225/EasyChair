package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Post;
import fr.upsaclay.easychair.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;
    private Post post;
    private Post post2;
    private List<Post> postList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test


        post = new Post();
        post.setId(1L);


        post2 = new Post();
        post2.setId(2L);


        postList = Arrays.asList(post, post2);

    }

}

