package ru.job4j.car.repository.car;

import ru.job4j.car.model.Car;
import ru.job4j.car.model.User;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return автомобиль с id.
     */
    Car create(Car car) throws Exception;

    /**
     * Обновить в базе автомобиль.
     * @param car автомобиль.
     * @return статус транзакции
     */
    boolean update(Car car);

    /**
     * Удалить автомобиль по id.
     * @param car ID
     * @return статус транзакции
     */
    boolean delete(Car car);

    /**
     * Список автомобилей отсортированных по id.
     * @return список автомобилей.
     */
    List<Car> findAll();

    /**
     * Найти автомобиль по ID
     * @return Optional of car.
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
     * @return Optional of car.
     */
    Optional<Car> findByName(String name);

    /**
     * Найти все автомобили пользователя
     * @param userId идентификатор пользователя
     * @return список автомобилей
     */
    List<Car> findByUserId(int userId);
}
