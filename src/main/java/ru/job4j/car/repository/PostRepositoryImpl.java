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
        if (!crudRepository.run(session -> session.persist(post))) {
            throw new RuntimeException("Ошибка при создании объявления.");
        }
        return post;
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
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.engine "
                        + "LEFT JOIN FETCH post.car.brand "
                        + "LEFT JOIN FETCH post.car.year "
                        + "LEFT JOIN FETCH post.car.body "
                        + "LEFT JOIN FETCH post.car.gearbox "
                        + "LEFT JOIN FETCH post.car.fuel "
                        + "LEFT JOIN FETCH post.car.color "
                        + "LEFT JOIN FETCH post.car.owner "
                        + "LEFT JOIN FETCH post.car.historyOwners "
                        + "WHERE post.id = :fId",
                Post.class, Map.of("fId", id)
        );
    }

    /**
     * Удалить объявление по идентификатору
     * @param post идентификатор объявления
     * @return статус транзакции
     */
    @Override
    public boolean delete(Post post) {
        return crudRepository.run(session -> session.delete(post));
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
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.engine "
                        + "LEFT JOIN FETCH post.car.brand "
                        + "LEFT JOIN FETCH post.car.year "
                        + "LEFT JOIN FETCH post.car.body "
                        + "LEFT JOIN FETCH post.car.gearbox "
                        + "LEFT JOIN FETCH post.car.fuel "
                        + "LEFT JOIN FETCH post.car.color "
                        + "LEFT JOIN FETCH post.car.owner "
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
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.engine "
                        + "LEFT JOIN FETCH post.car.brand "
                        + "LEFT JOIN FETCH post.car.year "
                        + "LEFT JOIN FETCH post.car.body "
                        + "LEFT JOIN FETCH post.car.gearbox "
                        + "LEFT JOIN FETCH post.car.fuel "
                        + "LEFT JOIN FETCH post.car.color "
                        + "LEFT JOIN FETCH post.car.owner "
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
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.engine "
                        + "LEFT JOIN FETCH post.car.brand "
                        + "LEFT JOIN FETCH post.car.year "
                        + "LEFT JOIN FETCH post.car.body "
                        + "LEFT JOIN FETCH post.car.gearbox "
                        + "LEFT JOIN FETCH post.car.fuel "
                        + "LEFT JOIN FETCH post.car.color "
                        + "LEFT JOIN FETCH post.car.owner "
                        + "WHERE post.status = false AND "
                        + "post.car.brand.name = :fBrand",
                Post.class, Map.of("fBrand", brand)
        );
    }

    /**
     * Показать объявления определенной марки.
     * @param brandId идентификатор марки автомобиля
     * @return объявления определенной марки.
     */
    @Override
    public List<Post> getPostsBrandId(int brandId) {
        return crudRepository.query(
                "FROM Post post "
                        + "LEFT JOIN FETCH post.priceHistories "
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.engine "
                        + "LEFT JOIN FETCH post.car.brand "
                        + "LEFT JOIN FETCH post.car.year "
                        + "LEFT JOIN FETCH post.car.body "
                        + "LEFT JOIN FETCH post.car.gearbox "
                        + "LEFT JOIN FETCH post.car.fuel "
                        + "LEFT JOIN FETCH post.car.color "
                        + "LEFT JOIN FETCH post.car.owner "
                        + "WHERE post.status = false AND "
                        + "post.car.brand.id = :fId",
                Post.class, Map.of("fId", brandId)
        );
    }

    /**
     * Объявления от заданного пользователя.
     * @param userId идентификатор пользователя
     * @return список объявлений пользователя user
     */
    @Override
    public List<Post> getPostsUserId(int userId) {
        return crudRepository.query(
                "FROM Post post "
                        + "LEFT JOIN FETCH post.priceHistories "
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.engine "
                        + "LEFT JOIN FETCH post.car.brand "
                        + "LEFT JOIN FETCH post.car.year "
                        + "LEFT JOIN FETCH post.car.body "
                        + "LEFT JOIN FETCH post.car.gearbox "
                        + "LEFT JOIN FETCH post.car.fuel "
                        + "LEFT JOIN FETCH post.car.color "
                        + "LEFT JOIN FETCH post.car.owner "
                        + "WHERE post.user.id = :fUserId",
                Post.class, Map.of("fUserId", userId)
        );
    }
}