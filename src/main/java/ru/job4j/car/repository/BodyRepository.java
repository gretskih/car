package ru.job4j.car.repository;

import ru.job4j.car.model.Body;

import java.util.List;
import java.util.Optional;

public interface BodyRepository {
    /**
     * Сохранить в базе
     * @param body кузов
     * @return кузов
     */
    Body create(Body body);

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
