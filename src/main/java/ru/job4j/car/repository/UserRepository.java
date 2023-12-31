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
     */
    void update(User user);

    /**
     * Удалить пользователя по id.
     * @param userId id пользователя
     */
    void delete(int userId);

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    List<User> findAllOrderById();

    /**
     * Найти пользователя по ID
     * @param userId id пользователя
     * @return Optional or user.
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
     * @return Optional or user.
     */
    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);
}
