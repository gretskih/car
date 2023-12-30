package ru.job4j.car.repository;

import ru.job4j.car.model.Post;

import java.util.List;

public interface PostRepository {
    /**
     * Сохранить в базе.
     * @param post объявление.
     * @return объявление с id.
     */
    Post create(Post post);

    List<Post> getPosts();

    /**
     * - показать объявления за последний день;
     */
    List<Post> getPostsLastDay();

    /**
     *  - показать объявления с фото;
     */
    List<Post> getPostsWithPhoto();

    /**
     * - показать объявления определенной марки.
     */
    List<Post> getPostsBrand(String brand);
}
