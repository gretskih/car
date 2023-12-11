package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.car.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            if (session != null && session.isOpen()) {
                session.getTransaction().rollback();
                session.close();
            }
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User SET login = :fLogin"
                                    + ", password = :fPassword WHERE id = :fId")
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("fPassword", user.getPassword())
                    .setParameter("fId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            if (session != null && session.isOpen()) {
                session.getTransaction().rollback();
                session.close();
            }
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :fId")
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            if (session != null && session.isOpen()) {
                session.getTransaction().rollback();
                session.close();
            }
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = null;
        List<User> userList = new ArrayList<>();
        try {
            session = sf.openSession();
            Query<User> query = session.createQuery("from User", User.class);
            userList = query.list();
            session.close();
        } catch (Exception e) {
            if (session != null && session.isOpen()) {
                session.getTransaction().rollback();
                session.close();
            }
        }
        return userList;
    }

//    /**
//     * Найти пользователя по ID
//     * @return пользователь.
//     */
//    public Optional<User> findById(int userId) {
//        return Optional.empty();
//    }
//
//    /**
//     * Список пользователей по login LIKE %key%
//     * @param key key
//     * @return список пользователей.
//     */
//    public List<User> findByLikeLogin(String key) {
//        return List.of();
//    }
//
//    /**
//     * Найти пользователя по login.
//     * @param login login.
//     * @return Optional or user.
//     */
//    public Optional<User> findByLogin(String login) {
//        return Optional.empty();
//    }
//
}
