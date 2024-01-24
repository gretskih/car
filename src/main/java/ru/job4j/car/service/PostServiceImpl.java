package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.mappers.PostPreviewMapper;
import ru.job4j.car.mappers.PostViewMapper;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostPreviewMapper postPreviewMapper;
    private final PostViewMapper postViewMapper;
    private final CarService carService;

    @Override
    public Post create(Post post, User user, Long price, Integer carId) {
        post.setCreated(LocalDateTime.now());
        post.setUser(user);
        addPriceHistory(post, price);
        try {
            addCar(post, carId);
            return postRepository.create(post);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void addPriceHistory(Post post, Long price) {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(price);
        priceHistory.setAfter(price);
        priceHistory.setCreated(LocalDateTime.now());
        post.setPriceHistories(Set.of(priceHistory));
    }

    /**
     * Это же дает гарантии, что объект будет при вставке данных. Поэтому не нужно.
     */
    private void addCar(Post post, Integer carId) {
        Optional<Car> carOptional = carService.findById(carId);
        if (carOptional.isEmpty()) {
            throw new ServiceException("Автомобиль не найден.");
        }
        post.setCar(carOptional.get());
    }

    @Override
    public boolean delete(int postId) {
        var postOptional = postRepository.findById(postId);
        return postOptional.filter(postRepository::delete).isPresent();
    }

    @Override
    public boolean changeStatus(int postId, boolean status) {
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
        List<Post> posts = postRepository.getPostsUserId(user.getId());
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostPreview> postPreviews = new ArrayList<>();
        for (Post post: posts.stream().distinct().toList()) {
            postPreviews.add(postPreviewMapper.getPostPreview(post.getCar(), post));
        }
        return postPreviews;
    }

    private boolean isEmpty(Collection<Photo> photos) {
        try {
            for (Photo photo : photos) {
                return Files.readAllBytes(Path.of(photo.getPath())).length != 0;
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return false;
    }
}
