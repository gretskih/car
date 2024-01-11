package ru.job4j.car.repository;

import ru.job4j.car.model.Gearbox;

import java.util.List;
import java.util.Optional;

public interface GearboxRepository {
    /**
     * Список коробок отсортированных по id.
     * @return список коробок.
     */
    List<Gearbox> findAllOrderById();

    /**
     * Найти коробку по ID
     * @return Optional of gearbox.
     */
    Optional<Gearbox> findById(int gearboxId);
}
