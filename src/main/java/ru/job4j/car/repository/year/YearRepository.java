package ru.job4j.car.repository.year;

import ru.job4j.car.model.Year;

import java.util.List;
import java.util.Optional;

public interface YearRepository {
    /**
     * Список лет отсортированных по id.
     * @return список лет.
     */
    List<Year> findAll();

    /**
     * Найти год по ID
     * @return Optional of year.
     */
    Optional<Year> findById(int yearId);
}
