package ru.job4j.car.repository.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    @Override
    public Optional<User> create(User user) {
        return crudRepository.optional(session -> {
            session.persist(user);
            return user;
        });
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     * @return статус транзакции
     */
    @Override
    public boolean update(User user) {
        return crudRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     * @return статус транзакции
     */
    @Override
    public boolean delete(int userId) {
        return crudRepository.run(
                "delete from User where id = :fId",
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    @Override
    public List<User> findAll() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    @Override
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    @Override
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "from User where login like :fKey", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    @Override
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User where login = :fLogin", User.class,
                Map.of("fLogin", login)
        );
    }

    /**
     * Найти пользователя по login и password.
     * @param login логин
     * @param password пароль
     * @return Optional or user.
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User where login = :fLogin AND password = :fPassword", User.class,
                Map.of("fLogin", login, "fPassword", password)
        );
    }
}
