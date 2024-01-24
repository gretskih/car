package ru.job4j.car.repository.body;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Body;
import ru.job4j.car.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateBodyRepository implements BodyRepository {
    private final CrudRepository crudRepository;

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
