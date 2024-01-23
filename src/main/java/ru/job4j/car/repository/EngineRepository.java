package ru.job4j.car.repository;

import ru.job4j.car.model.Engine;

import java.util.List;
import java.util.Optional;

public interface EngineRepository {
    /**
     * Сохранить в базе.
     * @param engine двигатель.
     * @return двигатель с id.
     */
    Engine create(Engine engine) throws Exception;

    /**
     * Обновить в базе двигатель.
     * @param engine двигатель.
     * @return статус транзакции
     */
    boolean update(Engine engine);

    /**
     * Удалить двигатель.
     * @param engine двигатель
     * @return статус транзакции
     */
    boolean delete(Engine engine);

    /**
     * Список двигателей отсортированных по id.
     * @return список двигателей.
     */
    List<Engine> findAll();

    /**
     * Найти двигатель по ID
     * @return Optional or engine.
     */
    Optional<Engine> findById(int engineId);

    /**
     * Список двигателей по name LIKE %key%
     * @param key key
     * @return список двигателей.
     */
    List<Engine> findByLikeName(String key);

    /**
     * Найти двигатель по name.
     * @param name name.
     * @return Optional of engine.
     */
    Optional<Engine> findByName(String name);
}
