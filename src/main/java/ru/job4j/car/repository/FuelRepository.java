package ru.job4j.car.repository;

import ru.job4j.car.model.Fuel;

import java.util.List;
import java.util.Optional;

public interface FuelRepository {

    /**
     * Список типов топлива отсортированных по id.
     * @return список типов топлива.
     */
    List<Fuel> findAll();

    /**
     * Найти топливо по ID
     * @return Optional of fuel.
     */
    Optional<Fuel> findById(int fuelId);
}
