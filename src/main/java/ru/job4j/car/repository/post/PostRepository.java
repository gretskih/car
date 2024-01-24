package ru.job4j.car.repository.post;

import ru.job4j.car.model.Photo;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    /**
     * Сохранить в базе.
     * @param post объявление.
     * @return объявление с id.
     */
    Post create(Post post);

    /**
     * Найти по идентификатору
     * @param id идентификатор объявления
     * @return optional объявление
     */
    Optional<Post> findById(int id);

    /**
     * Удалить объявление по идентификатору
     * @param post идентификатор объявления
     * @return статус транзакции
     */
    boolean delete(Post post);

    /**
     * Установить статус объявлению
     * @param postId идентификатор объявления
     * @param status статус объявления
     * @return статус транзакции
     */
    boolean setStatus(int postId, boolean status);

    /**
     * Показать все объявления
     * @return список объявлений
     */
    List<Post> getPosts();

    /**
     * Показать объявления за последний день;
     * @return список объявлений
     */
    List<Post> getPostsLastDay();

    /**
     * Показать объявления определенной марки.
     * @param brand марка автомобиля
     * @return объявления марки brand.
     */
    List<Post> getPostsBrand(String brand);

    /**
     * Показать объявления определенной марки.
     * @param brandId дентификатор марки автомобиля
     * @return объявления марки brand.
     */
    List<Post> getPostsBrandId(int brandId);

    /**
     * Объявления от заданного пользователя.
     * @param userId идентификатор пользователя
     * @return список объявлений пользователя user
     */
    List<Post> getPostsUserId(int userId);
}
