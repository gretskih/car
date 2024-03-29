package ru.job4j.car.service.post;

import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post create(Post post, User user, Long price, Integer carId) throws Exception;

    boolean delete(int postId);

    boolean changeStatus(int postId, boolean status);

    Optional<PostView> findById(int id);

    List<PostPreview> findAllPostPreview();

    List<PostPreview> getPostPreviewsLastDay();

    List<PostPreview> getPostPreviewsWithPhoto();

    List<PostPreview> getPostPreviewsUser(User user);

    List<PostPreview> getPostsPreviewsBrandId(int brandId);
}
