package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.mappers.PostPreviewMapper;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;
import ru.job4j.car.repository.PostRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostPreviewMapper postPreviewMapper;

    @Override
    public Post create(Post post) {
        return postRepository.create(post);
    }

    @Override
    public List<PostPreview> findAllPostPreview() {
        List<Post> posts = postRepository.getPosts();
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostPreview> postPreviews = new ArrayList<>();
        for (Post post: posts) {
            postPreviews.add(postPreviewMapper.getCarPreview(post.getCar(), post));
        }
        return postPreviews;
    }
}
