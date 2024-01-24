package ru.job4j.car.repository.color;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Color;
import ru.job4j.car.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateColorRepository implements ColorRepository {
    private final CrudRepository crudRepository;

    /**
     * Список цветов отсортированных по id.
     * @return список цветов.
     */
    @Override
    public List<Color> findAll() {
        return crudRepository.query("from Color order by id asc", Color.class);
    }

    /**
     * Найти цвет по ID
     * @return Optional or colour.
     */
    @Override
    public Optional<Color> findById(int colorId) {
        return crudRepository.optional(
                "from Color where id = :fId", Color.class, Map.of("fId", colorId)
        );
    }
}
