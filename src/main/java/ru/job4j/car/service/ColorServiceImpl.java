package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Color;
import ru.job4j.car.repository.ColorRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    /**
     * Список цветов отсортированных по id.
     * @return список цветов.
     */
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    /**
     * Найти цвет по ID
     * @return Optional or colour.
     */
    public Optional<Color> findById(int colorId) {
        return colorRepository.findById(colorId);
    }
}
