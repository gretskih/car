package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Year;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class YearRepositoryImpl implements YearRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе
     * @param year год
     * @return Year или null
     */
    @Override
    public Year create(Year year) {
        if (crudRepository.run(session -> session.persist(year))) {
            return year;
        }
        return null;
    }

    /**
     * Список лет отсортированных по id.
     * @return список лет.
     */
    @Override
    public List<Year> findAllOrderById() {
        return crudRepository.query(
                "from Year order by id desc", Year.class
        );
    }

    /**
     * Найти год по ID
     * @return Optional of year.
     */
    @Override
    public Optional<Year> findById(int yearId) {
        return crudRepository.optional(
                "from Year where id = :fId", Year.class, Map.of("fId", yearId)
        );
    }
}
