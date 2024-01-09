package ru.job4j.car.service;

import ru.job4j.car.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerService {
    /**
     * Сохранить в базе.
     * @param owner владелец.
     * @return владелец с id.
     */
    Owner create(Owner owner);

    /**
     * Обновить в базе владельца.
     * @param owner владелец.
     */
    void update(Owner owner);

    /**
     * Удалить владельца по id.
     * @param ownerId ID
     */
    void delete(int ownerId);

    /**
     * Список владельцев отсортированных по id.
     * @return список владельцев.
     */
    List<Owner> findAllOrderById();

    /**
     * Найти владельца по ID
     * @return  Optional or owner.
     */
    Optional<Owner> findById(int ownerId);

    /**
     * Список владельцев по name LIKE %key%
     * @param key key
     * @return список владельцев.
     */
    List<Owner> findByLikeName(String key);

    /**
     * Найти владельца по name.
     * @param name name.
     * @return Optional or owner.
     */
    Optional<Owner> findByName(String name);

    /**
     * Найти владельца по userId
     * @return владелец.
     */
    Optional<Owner> findByUserId(int userId);
}
