package ru.job4j.car.service;

import ru.job4j.car.model.Body;

import java.util.List;
import java.util.Optional;

public interface BodyService {
    /**
     * Список кузовов отсортированных по типу.
     * @return список кузовов.
     */
    List<Body> findAllOrderByType();

    /**
     * Найти кузов по ID
     * @return Optional or body.
     */
    Optional<Body> findById(int bodyId);
}
