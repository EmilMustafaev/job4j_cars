package ru.job4j.cars.repository.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class EngineRepository {
    private final CrudRepository crudRepository;

    public Optional<Engine> findById(int id) {
        return crudRepository.optional(
                "FROM Engine WHERE id = :id",
                Engine.class,
                Map.of("id", id)
        );
    }

    public List<Engine> findAll() {
        return crudRepository.query(
                "FROM Engine",
                Engine.class
        );
    }

    public void save(Engine engine) {
        crudRepository.run(session -> session.save(engine));
    }

    public void update(Engine engine) {
        crudRepository.run(session -> session.update(engine));
    }

    public void delete(int id) {
        crudRepository.run(
                "DELETE FROM Engine WHERE id = :id",
                Map.of("id", id)
        );
    }
}
