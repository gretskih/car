package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Brand;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {
    private final CrudRepository crudRepository;

    /**
     * Список производителей отсортированных по id.
     * @return список производителей.
     */
    @Override
    public List<Brand> findAll() {
        return crudRepository.query("from Brand order by id asc", Brand.class);
    }

    /**
     * Найти производителя по ID
     * @return Optional of brand.
     */
    @Override
    public Optional<Brand> findById(int brandId) {
        return crudRepository.optional("from Brand where id = :fId",
                Brand.class, Map.of("fId", brandId));
    }
}
