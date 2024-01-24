package ru.job4j.car.repository.body;

import ru.job4j.car.model.Body;

import java.util.List;
import java.util.Optional;

public interface BodyRepository {

    /**
     * Список кузовов отсортированных по типу.
     * @return список кузовов.
     */
    List<Body> findAll();

    /**
     * Найти кузов по ID
     * @return Optional of body.
     */
    Optional<Body> findById(int bodyId);
}
