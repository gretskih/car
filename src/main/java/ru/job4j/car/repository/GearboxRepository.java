package ru.job4j.car.repository;

import ru.job4j.car.model.Gearbox;

import java.util.List;
import java.util.Optional;

public interface GearboxRepository {
    /**
     * Сохранить в базе
     * @param gearbox коробка
     * @return Gearbox или null
     */
    Gearbox create(Gearbox gearbox);

    /**
     * Список коробок отсортированных по id.
     * @return список коробок.
     */
    List<Gearbox> findAll();

    /**
     * Найти коробку по ID
     * @return Optional of gearbox.
     */
    Optional<Gearbox> findById(int gearboxId);
}
