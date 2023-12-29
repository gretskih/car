package ru.job4j.car.repository;

import ru.job4j.car.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return автомобиль с id.
     */
    Car create(Car car);

    /**
     * Обновить в базе автомобиль.
     * @param car автомобиль.
     */
    void update(Car car);

    /**
     * Удалить автомобиль по id.
     * @param carId ID
     */
    void delete(int carId);

    /**
     * Список автомобилей отсортированных по id.
     * @return список автомобилей.
     */
    List<Car> findAllOrderById();

    /**
     * Найти автомобиль по ID
     * @return автомобиль.
     */
    Optional<Car> findById(int carId);

    /**
     * Список автомобилей по name LIKE %key%
     * @param key key
     * @return список автомобилей.
     */
    List<Car> findByLikeName(String key);

    /**
     * Найти автомобиль по name.
     * @param name name.
     * @return Optional or car.
     */
    Optional<Car> findByName(String name);
}
