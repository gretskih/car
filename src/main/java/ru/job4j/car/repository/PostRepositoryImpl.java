package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param post объявление.
     * @return объявление с id.
     */
    @Override
    public Post create(Post post) {
        if (crudRepository.run(session -> session.persist(post))) {
            return post;
        }
        return null;
    }

    /**
     * Найти по идентификатору
     * @param id идентификатор объявления
     * @return optional объявление
     */
    @Override
    public Optional<Post> findById(int id) {
        return crudRepository.optional(
                "FROM Post post "
                        + "LEFT JOIN FETCH post.priceHistories "
                        + "LEFT JOIN FETCH post.participates "
                        + "LEFT JOIN FETCH post.car.owners "
                        + "LEFT JOIN FETCH post.car.photos "
                        + "WHERE post.id = :fId",
                Post.class, Map.of("fId", id)
        );
    }

    /**
     * Удалить объявление по идентификатору
     * @param postId идентификатор объявления
     * @return статус транзакции
     */
    @Override
    public boolean delete(int postId) {
        return crudRepository.run(
                "delete from Post where id = :fId",
                Map.of("fId", postId)
        );
    }

    /**
     * Установить статус объявлению
     * @param postId идентификатор объявления
     * @param status статус объявления
     * @return статус транзакции
     */
    @Override
    public boolean setStatus(int postId, boolean status) {
        return crudRepository.run(
                "UPDATE Post SET status = :fStatus WHERE id = :fId",
                Map.of("fStatus", status, "fId", postId)
        );
    }

    /**
     * Показать все объявления
     * @return список объявлений
     */
    @Override
    public List<Post> getPosts() {
        return crudRepository.query(
                "FROM Post post "
                        + "LEFT JOIN FETCH post.priceHistories "
                        + "LEFT JOIN FETCH post.participates "
                        + "LEFT JOIN FETCH post.car.owners "
                        + "LEFT JOIN FETCH post.car.photos "
                        + "WHERE post.status = false",
                Post.class
        );
    }

    /**
     * Показать объявления за последний день;
     * @return список объявлений
     */
    @Override
    public List<Post> getPostsLastDay() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        return crudRepository.query(
                "FROM Post post "
                        + "LEFT JOIN FETCH post.priceHistories "
                        + "LEFT JOIN FETCH post.participates "
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.owners "
                        + "WHERE post.status = false AND "
                        + "post.created > :fLocalDateTimeNow",
                Post.class, Map.of("fLocalDateTimeNow", localDateTimeNow.minusDays(1))
        );
    }

    /**
     * Показать объявления определенной марки.
     * @param brand марка автомобиля
     * @return объявления определенной марки.
     */
    @Override
    public List<Post> getPostsBrand(String brand) {
        return crudRepository.query(
                "FROM Post post "
                        + "LEFT JOIN FETCH post.priceHistories "
                        + "LEFT JOIN FETCH post.participates "
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.owners "
                        + "WHERE post.status = false AND "
                        + "post.car.brand.name = :fBrand",
                Post.class, Map.of("fBrand", brand)
        );
    }

    /**
     * Объявления от заданного пользователя.
     * @param user пользователь
     * @return список объявлений пользователя user
     */
    @Override
    public List<Post> getPostsUser(User user) {
        return crudRepository.query(
                "FROM Post post "
                        + "LEFT JOIN FETCH post.priceHistories "
                        + "LEFT JOIN FETCH post.participates "
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.owners "
                        + "WHERE post.user = :fUser",
                Post.class, Map.of("fUser", user)
        );
    }
}