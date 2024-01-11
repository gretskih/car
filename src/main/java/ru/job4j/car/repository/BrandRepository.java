package ru.job4j.car.repository;

import ru.job4j.car.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    /**
     * Список производителей отсортированных по id.
     * @return список производителей.
     */
    List<Brand> findAllOrderById();

    /**
     * Найти производителя по ID
     * @return Optional of brand.
     */
    Optional<Brand> findById(int brandId);
}
