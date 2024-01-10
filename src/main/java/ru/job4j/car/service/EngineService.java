package ru.job4j.car.service;

import ru.job4j.car.model.Engine;

import java.util.List;
import java.util.Optional;

public interface EngineService {

    Engine create(Engine engine);

    List<Engine> findAllOrderById();

    Optional<Engine> findById(int engineId);
}
