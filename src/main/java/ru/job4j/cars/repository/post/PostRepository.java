package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class PostRepository  {

    private final CrudRepository crudRepository;

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public Optional<Post> create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return Optional.of(post);
    }

    public boolean update(Post post) {
        return crudRepository.tx(session -> {
            int result = session.createQuery(
                            "UPDATE Post SET description = :fDescription, hasPhoto = :fHasPhoto WHERE id = :fId")
                    .setParameter("fDescription", post.getDescription())
                    .setParameter("fHasPhoto", post.isHasPhoto())
                    .setParameter("fId", post.getId())
                    .executeUpdate();
            return result > 0;
        });
    }


    public boolean delete(Integer id) {
        return crudRepository.tx(session -> {
            int result = session.createQuery("DELETE FROM Post WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            return result > 0;
        });
    }

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
