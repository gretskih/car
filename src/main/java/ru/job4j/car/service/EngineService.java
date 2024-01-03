package ru.job4j.car.service;

import ru.job4j.car.model.Engine;

import java.util.List;
import java.util.Optional;

public interface EngineService {

    Engine create(Engine engine);

    List<Engine> findAllOrderById();

    /**
     * Найти двигатель по ID
     * @return Optional or engine.
     */
    Optional<Engine> findById(int engineId);
}
