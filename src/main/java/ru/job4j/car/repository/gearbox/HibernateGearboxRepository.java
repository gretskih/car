package ru.job4j.car.repository.gearbox;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Gearbox;
import ru.job4j.car.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateGearboxRepository implements GearboxRepository {
    private final CrudRepository crudRepository;

    /**
     * Список коробок отсортированных по id.
     * @return список коробок.
     */
    @Override
    public List<Gearbox> findAll() {
        return crudRepository.query(
                "from Gearbox order by id asc", Gearbox.class
        );
    }

    /**
     * Найти коробку по ID
     * @return Optional of gearbox.
     */
    @Override
    public Optional<Gearbox> findById(int gearboxId) {
        return crudRepository.optional(
                "from Gearbox where id = :fId", Gearbox.class, Map.of("fId", gearboxId)
        );
    }
}
