package ru.job4j.car.repository;

import ru.job4j.car.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    Optional<User> create(User user);

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     * @return статус транзакции
     */
    boolean update(User user);

    /**
     * Удалить пользователя по id.
     * @param userId id пользователя
     * @return статус транзакции
     */
    boolean delete(int userId);

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    List<User> findAllOrderById();

    /**
     * Найти пользователя по ID
     * @param userId id пользователя
     * @return Optional of user.
     */
    Optional<User> findById(int userId);

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    List<User> findByLikeLogin(String key);

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional of user.
     */
    Optional<User> findByLogin(String login);

    /**
     * Найти пользователя по login и password.
     * @param login логин
     * @param password пароль
     * @return Optional of user.
     */
    Optional<User> findByLoginAndPassword(String login, String password);
}
