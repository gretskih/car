package ru.job4j.car.repository;

import ru.job4j.car.model.Color;

import java.util.List;
import java.util.Optional;

public interface ColorRepository {
    /**
     * Сохранить в базе
     * @param color цвет
     * @return цвет
     */
    Color create(Color color);

    /**
     * Список цветов отсортированных по id.
     * @return список цветов.
     */
    List<Color> findAllOrderById();

    /**
     * Найти цвет по ID
     * @return Optional of colour.
     */
    Optional<Color> findById(int colorId);
}
