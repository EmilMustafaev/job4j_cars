package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
public class PostRepository {

    private final CrudRepository crudRepository;

    public List<Post> findPostsLastDay() {
        String query = """
            FROM Post p WHERE p.created >= :yesterday
        """;
        return crudRepository.query(query, Post.class, Map.of("yesterday", LocalDateTime.now().minusDays(1)));
    }

    public List<Post> findPostsWithPhotos() {
        String query = """
        FROM Post p WHERE p.hasPhoto = true
    """;
        return crudRepository.query(query, Post.class);
    }

    public List<Post> findPostsByCarBrand(String brandName) {
        String query = """
            FROM Post p WHERE p.car.name = :brandName
        """;
        return crudRepository.query(query, Post.class, Map.of("brandName", brandName));
    }

}
