package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepositoryImpl implements EngineRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param engine двигатель.
     * @return двигатель с id.
     */
    @Override
    public Engine create(Engine engine) {
        if (crudRepository.run(session -> session.persist(engine))) {
            return engine;
        }
        return null;
    }

    /**
     * Обновить в базе двигатель.
     * @param engine двигатель.
     */
    @Override
    public boolean update(Engine engine) {
        return crudRepository.run(session -> session.merge(engine));
    }

    /**
     * Удалить двигатель по id.
     * @param engineId ID
     */
    @Override
    public boolean delete(int engineId) {
        return crudRepository.run(
                "delete from Engine where id = :fId",
                Map.of("fId", engineId)
        );
    }

    /**
     * Список двигателей отсортированных по id.
     * @return список двигателей.
     */
    @Override
    public List<Engine> findAllOrderById() {
        return crudRepository.query("from Engine order by id asc", Engine.class);
    }

    /**
     * Найти двигатель по ID
     * @return двигатель.
     */
    @Override
    public Optional<Engine> findById(int engineId) {
        return crudRepository.optional(
                "from Engine where id = :fId", Engine.class,
                Map.of("fId", engineId)
        );
    }

    /**
     * Список двигателей по name LIKE %key%
     * @param key key
     * @return список двигателей.
     */
    @Override
    public List<Engine> findByLikeName(String key) {
        return crudRepository.query(
                "from Engine where name like :fKey", Engine.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти двигатель по name.
     * @param name name.
     * @return Optional or engine.
     */
    @Override
    public Optional<Engine> findByName(String name) {
        return crudRepository.optional(
                "from Engine where name = :fName", Engine.class,
                Map.of("fName", name)
        );
    }
}
