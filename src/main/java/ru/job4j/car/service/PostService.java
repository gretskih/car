package ru.job4j.car.service;

import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.model.Post;

import java.util.List;

public interface PostService {
    Post create(Post post);

    List<PostPreview> findAllPostPreview();
}
