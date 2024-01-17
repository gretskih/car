package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Body;
import ru.job4j.car.model.Photo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BodyRepositoryImpl implements BodyRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе
     * @param body кузов
     * @return кузов
     */
    @Override
    public Body create(Body body) {
        if (crudRepository.run(session -> session.persist(body))) {
            return body;
        }
        return null;
    }

    /**
     * Список кузовов отсортированных по типу.
     * @return список кузовов.
     */
    @Override
    public List<Body> findAll() {
        return crudRepository.query(
                "from Body order by bodyType asc", Body.class
        );
    }

    /**
     * Найти кузов по ID
     * @return Optional of body.
     */
    @Override
    public Optional<Body> findById(int bodyId) {
        return crudRepository.optional(
                "from Body where id = :fId", Body.class, Map.of("fId", bodyId)
        );
    }
}
