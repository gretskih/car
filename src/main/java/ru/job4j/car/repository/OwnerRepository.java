package ru.job4j.car.repository;

import ru.job4j.car.model.Owner;
import ru.job4j.car.model.User;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository {
    /**
     * Сохранить в базе.
     * @param owner владелец.
     * @return владелец с id.
     */
    Owner create(Owner owner);

    /**
     * Обновить в базе владельца.
     * @param owner владелец.
     * @return статус транзакции
     */
    boolean update(Owner owner);

    /**
     * Удалить владельца по id.
     * @param owner ID
     * @return статус транзакции
     */
    boolean delete(Owner owner);

    /**
     * Список владельцев отсортированных по id.
     * @return список владельцев.
     */
    List<Owner> findAllOrderById();

    /**
     * Найти владельца по ID
     * @return  Optional of owner.
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
     * @return Optional of owner.
     */
    Optional<Owner> findByName(String name);

    /**
     * Найти владельца по userId
     * @return Optional of owner.
     */
    Optional<Owner> findByUser(User user);
}
