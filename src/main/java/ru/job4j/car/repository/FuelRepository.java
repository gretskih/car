package ru.job4j.car.repository;

import ru.job4j.car.model.Fuel;

import java.util.List;
import java.util.Optional;

public interface FuelRepository {

    /**
     * Сохранить в базе
     * @param fuel топливо
     * @return Fuel
     */
    Fuel create(Fuel fuel);

    /**
     * Список типов топлива отсортированных по id.
     * @return список типов топлива.
     */
    List<Fuel> findAllOrderById();

    /**
     * Найти топливо по ID
     * @return Optional of fuel.
     */
    Optional<Fuel> findById(int fuelId);
}