package ru.job4j.car.repository;

import ru.job4j.car.model.Year;

import java.util.List;
import java.util.Optional;

public interface YearRepository {
    /**
     * Сохранить в базе
     * @param year год
     * @return Year или null
     */
    Year create(Year year);

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
