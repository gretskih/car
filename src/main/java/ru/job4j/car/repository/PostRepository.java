package ru.job4j.car.repository;

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

    Optional<Post> findById(int id);

    boolean delete(int postId);

    boolean setStatus(int postId, boolean status);

    List<Post> getPosts();

    /**
     * - показать объявления за последний день;
     */
    List<Post> getPostsLastDay();

    /**
     * - показать объявления определенной марки.
     */
    List<Post> getPostsBrand(String brand);

    List<Post> getPostsUser(User user);
}
