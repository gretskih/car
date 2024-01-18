package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.mappers.PostPreviewMapper;
import ru.job4j.car.mappers.PostViewMapper;
import ru.job4j.car.model.Photo;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostPreviewMapper postPreviewMapper;
    private final PostViewMapper postViewMapper;

    @Override
    public Post create(Post post) {
        try {
            return postRepository.create(post);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean delete(int postId) {
        var postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            return postRepository.delete(postOptional.get());
        }
        return false;
    }

    @Override
    public boolean setStatus(int postId, boolean status) {
        return postRepository.setStatus(postId, status);
    }

    @Override
    public Optional<PostView> findById(int id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return Optional.empty();
        }
        Post post = postOptional.get();
        return Optional.of(postViewMapper.getPostView(post, post.getCar(), post.getUser()));
    }

    @Override
    public List<PostPreview> findAllPostPreview() {
        List<Post> posts = postRepository.getPosts();
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostPreview> postPreviews = new ArrayList<>();
        for (Post post: posts.stream().distinct().toList()) {
            postPreviews.add(postPreviewMapper.getPostPreview(post.getCar(), post));
        }
        return postPreviews;
    }

    @Override
    public List<PostPreview> getPostPreviewsLastDay() {
        List<Post> posts = postRepository.getPostsLastDay();
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostPreview> postPreviews = new ArrayList<>();
        for (Post post: posts.stream().distinct().toList()) {
            postPreviews.add(postPreviewMapper.getPostPreview(post.getCar(), post));
        }
        return postPreviews;
    }

    @Override
    public List<PostPreview> getPostsPreviewsBrandId(int brandId) {
        List<Post> posts = postRepository.getPostsBrandId(brandId);
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostPreview> postPreviews = new ArrayList<>();
        for (Post post: posts.stream().distinct().toList()) {
            postPreviews.add(postPreviewMapper.getPostPreview(post.getCar(), post));
        }
        return postPreviews;
    }

    @Override
    public List<PostPreview> getPostPreviewsWithPhoto() {
        List<Post> posts = postRepository.getPosts();
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        posts = posts.stream().distinct().toList();

        List<PostPreview> postPreviews = new ArrayList<>();
        for (Post post: posts.stream().distinct().toList()) {
            if (isEmpty(post.getCar().getPhotos())) {
                postPreviews.add(postPreviewMapper.getPostPreview(post.getCar(), post));
            }
        }
        return postPreviews;
    }

    @Override
    public List<PostPreview> getPostPreviewsUser(User user) {
        List<Post> posts = postRepository.getPostsUser(user);
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostPreview> postPreviews = new ArrayList<>();
        for (Post post: posts.stream().distinct().toList()) {
            postPreviews.add(postPreviewMapper.getPostPreview(post.getCar(), post));
        }
        return postPreviews;
    }

    public boolean isEmpty(Collection<Photo> photos) {
        try {
            for (Photo photo : photos) {
                return Files.readAllBytes(Path.of(photo.getPath())).length != 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
