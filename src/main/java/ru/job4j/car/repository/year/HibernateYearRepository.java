package ru.job4j.car.repository.year;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Year;
import ru.job4j.car.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateYearRepository implements YearRepository {
    private final CrudRepository crudRepository;

    /**
     * Список лет отсортированных по id.
     * @return список лет.
     */
    @Override
    public List<Year> findAll() {
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
