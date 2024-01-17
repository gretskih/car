package ru.job4j.car.service;

import ru.job4j.car.model.Year;

import java.util.List;
import java.util.Optional;

public interface YearService {

    /**
     * Список лет отсортированных по id.
     * @return список лет.
     */
    List<Year> findAll();

    /**
     * Найти год по ID
     * @return Optional or year.
     */
    Optional<Year> findById(int yearId);
}
