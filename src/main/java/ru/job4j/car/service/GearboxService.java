package ru.job4j.car.service;

import ru.job4j.car.model.Gearbox;

import java.util.List;
import java.util.Optional;

public interface GearboxService {
    /**
     * Список коробок отсортированных по id.
     * @return список трансмиссий.
     */
    List<Gearbox> findAll();

    /**
     * Найти коробку по ID
     * @return Optional or gearbox.
     */
    Optional<Gearbox> findById(int gearboxId);
}
