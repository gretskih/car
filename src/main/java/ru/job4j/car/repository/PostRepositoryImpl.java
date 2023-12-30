package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Post;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository{

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param post объявление.
     * @return объявление с id.
     */
    @Override
    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
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
                        + "LEFT JOIN FETCH post.car.photos "
                        + "LEFT JOIN FETCH post.car.owners ",
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
                  + "WHERE post.created > :fLocalDateTimeNow",
                Post.class, Map.of("fLocalDateTimeNow", localDateTimeNow.minusDays(1))
        );
    }

    /**
     * Показать объявления с фото;
     * @return список объявлений
     */
    @Override
     public List<Post> getPostsWithPhoto() {
         return crudRepository.query(
                 "FROM Post post "
                         + "LEFT JOIN FETCH post.priceHistories "
                         + "LEFT JOIN FETCH post.participates "
                         + "LEFT JOIN FETCH post.car.photos "
                         + "LEFT JOIN FETCH post.car.owners "
                         + "WHERE size(post.car.photos) > 0",
                 Post.class
         );
     }

    /**
     * Показать объявления определенной марки.
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
                        + "WHERE post.car.brand = :fBrand",
                Post.class, Map.of("fBrand", brand)
        );
    }
}