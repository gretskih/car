package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Gearbox;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class GearboxRepositoryImpl implements GearboxRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Gearbox> findAllOrderById() {
        return crudRepository.query(
                "from Gearbox order by id asc", Gearbox.class
        );
    }

    @Override
    public Optional<Gearbox> findById(int gearboxId) {
        return crudRepository.optional(
                "from Gearbox where id = :fId", Gearbox.class, Map.of("fId", gearboxId)
        );
    }
}
