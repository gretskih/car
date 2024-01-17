package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Fuel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class FuelRepositoryImpl implements FuelRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе
     * @param fuel топливо
     * @return Fuel или null
     */
    @Override
    public Fuel create(Fuel fuel) {
        if (crudRepository.run(session -> session.persist(fuel))) {
            return fuel;
        }
        return null;
    }

    /**
     * Список типов топлива отсортированных по id.
     * @return List список типов топлива.
     */
    @Override
    public List<Fuel> findAll() {
        return crudRepository.query("from Fuel order by id asc", Fuel.class
        );
    }

    /**
     * Найти топливо по ID
     * @return Optional of fuel.
     */
    @Override
    public Optional<Fuel> findById(int fuelId) {
        return crudRepository.optional(
                "from Fuel where id = :fId", Fuel.class,
                Map.of("fId", fuelId)
        );
    }
}
