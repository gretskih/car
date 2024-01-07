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
     *
     * @param post объявление.
     * @return объявление с id.
     */
    @Override
    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

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

    @Override
    public void delete(int postId) {
        crudRepository.run(
                "delete from Post where id = :fId",
                Map.of("fId", postId)
        );
    }

    @Override
    public void setStatus(int postId, boolean status) {
        crudRepository.run(
                "UPDATE Post SET status = :fStatus WHERE id = :fId",
                Map.of("fStatus", status, "fId", postId)
        );
    }

    /**
     * Показать все объявления
     *
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
     *
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
     *
     * @param brand марка автомобиля
     * @return показать объявления определенной марки.
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
                        + "post.car.brand = :fBrand",
                Post.class, Map.of("fBrand", brand)
        );
    }

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