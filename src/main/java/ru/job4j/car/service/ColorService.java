package ru.job4j.car.service;

import ru.job4j.car.model.Color;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    /**
     * Список цветов отсортированных по id.
     * @return список цветов.
     */
    List<Color> findAllOrderById();

    /**
     * Найти цвет по ID
     * @return Optional or colour.
     */
    Optional<Color> findById(int colorId);
}
