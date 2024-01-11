package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Body;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BodyRepositoryImpl implements BodyRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Body> findAllOrderByType() {
        return crudRepository.query(
                "from Body order by bodyType asc", Body.class
        );
    }

    @Override
    public Optional<Body> findById(int bodyId) {
        return crudRepository.optional(
                "from Body where id = :fId", Body.class, Map.of("fId", bodyId)
        );
    }
}
