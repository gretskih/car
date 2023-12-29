package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import ru.job4j.car.model.Post;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
public class PostRepositoryImpl {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param post объявление.
     * @return объявление с id.
     */
    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

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
     * - показать объявления за последний день;
    */
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
     *  - показать объявления с фото;
     */
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
     * - показать объявления определенной марки.
     */
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