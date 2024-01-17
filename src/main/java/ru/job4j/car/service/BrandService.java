package ru.job4j.car.service;

import ru.job4j.car.model.Brand;

import java.util.List;

public interface BrandService {
    /**
     * Список производителей отсортированных по id.
     * @return список производителей.
     */
    List<Brand> findAll();
}
