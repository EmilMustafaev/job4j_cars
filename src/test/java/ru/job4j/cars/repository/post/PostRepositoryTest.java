package ru.job4j.cars.repository.post;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {

    private CrudRepository crudRepository;
    private PostRepository postRepository;
    private SessionFactory sessionFactory;

    @BeforeAll
    void setUp() {
        sessionFactory = new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        crudRepository = new CrudRepository(sessionFactory);
        postRepository = new PostRepository(crudRepository);
    }

    @AfterAll
    void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @AfterEach
    void cleanUpDatabase() {
        crudRepository.run("DELETE FROM Post", Map.of());
    }


    @Test
    void whenCreatePostThenFindIt() {
        Post post = new Post();
        post.setDescription("Test Post");
        post.setCreated(LocalDateTime.now());
        post.setHasPhoto(true);

        Optional<Post> savedPost = postRepository.create(post);
        assertTrue(savedPost.isPresent());
        assertEquals(post.getDescription(), savedPost.get().getDescription());
    }

    @Test
    void whenUpdatePostThenVerifyChanges() {
        Post post = new Post();
        post.setDescription("Initial Description");
        post.setCreated(LocalDateTime.now());
        post.setHasPhoto(false);

        Optional<Post> savedPost = postRepository.create(post);
        assertTrue(savedPost.isPresent());

        Post updatedPost = savedPost.get();
        updatedPost.setDescription("Updated Description");
        updatedPost.setHasPhoto(true);

        boolean isUpdated = postRepository.update(updatedPost);
        assertTrue(isUpdated);

        List<Post> allPosts = postRepository.findPostsWithPhotos();
        assertEquals(1, allPosts.size());
        assertEquals("Updated Description", allPosts.get(0).getDescription());
    }

    @Test
    void whenDeletePostThenCannotFindIt() {
        Post post = new Post();
        post.setDescription("Test Delete");
        post.setCreated(LocalDateTime.now());

        Optional<Post> savedPost = postRepository.create(post);
        assertTrue(savedPost.isPresent());

        boolean isDeleted = postRepository.delete(savedPost.get().getId());
        assertTrue(isDeleted);

        Optional<Post> deletedPost = crudRepository.optional("FROM Post WHERE id = :id", Post.class,
                Map.of("id", savedPost.get().getId()));
        assertTrue(deletedPost.isEmpty());
    }

    @Test
    void whenFindPostsLastDayThenReturnRecentPosts() {
        Post post = new Post();
        post.setDescription("Recent Post");
        post.setCreated(LocalDateTime.now().minusHours(12));

        postRepository.create(post);

        List<Post> posts = postRepository.findPostsLastDay();
        assertFalse(posts.isEmpty());
        assertEquals("Recent Post", posts.get(0).getDescription());
    }

    @Test
    void whenFindPostsWithPhotosThenReturnPosts() {
        Post post1 = new Post();
        post1.setDescription("Photo Post");
        post1.setCreated(LocalDateTime.now());
        post1.setHasPhoto(true);

        Post post2 = new Post();
        post2.setDescription("No Photo Post");
        post2.setCreated(LocalDateTime.now());
        post2.setHasPhoto(false);

        postRepository.create(post1);
        postRepository.create(post2);

        List<Post> posts = postRepository.findPostsWithPhotos();
        assertEquals(1, posts.size());
        assertEquals("Photo Post", posts.get(0).getDescription());
    }

    @Test
    void whenFindPostsByCarBrandThenReturnMatchingPosts() {
        Car car = new Car();
        Engine engine = new Engine();
        engine.setName("V8");
        car.setName("Toyota");
        car.setEngine(engine);
        crudRepository.run(session -> session.persist(car));

        Post post = new Post();
        post.setDescription("Car Post");
        post.setCreated(LocalDateTime.now());
        post.setCar(car);

        postRepository.create(post);

        List<Post> posts = postRepository.findPostsByCarBrand("Toyota");
        assertEquals(1, posts.size());
        assertEquals("Car Post", posts.get(0).getDescription());
    }
}
