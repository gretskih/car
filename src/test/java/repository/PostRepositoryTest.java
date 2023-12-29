package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.PostRepositoryImpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostRepositoryTest {

    private final PostRepositoryImpl postRepository = new PostRepositoryImpl(ConfigurationTest.crudRepository);

    /**
     * Очистка базы
     */
    @BeforeEach
    public void clearTablesBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    static Post getPost(String postName) {
        Car car = CarRepositoryTest.getCar("Car" + postName);

        User user = new User();
        user.setLogin("login" + postName);
        user.setPassword("pass");

        Post post = new Post();
        post.setDescription("description");
        post.setCreated(LocalDateTime.now());
        post.setUser(user);
        post.setCar(car);

        return post;
    }

    /**
     * - показать объявления за последний день;
     */
    @Test
    public void whenGetPostsLastDayThenGetPostLastDay() {
        Post expectedPost = getPost("post1");
        postRepository.create(expectedPost);

        Post post = getPost("post2");
        post.setCreated(LocalDateTime.now().minusDays(2));
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsLastDay();

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     *  - показать объявления с фото;
     */
    @Test
    public void whenGetPostsWithPhotoThenGetPostWithPhoto() {
        Post expectedPost = getPost("post3");
        postRepository.create(expectedPost);

        Post post = getPost("post4");
        post.getCar().setPhotos(new HashSet<>());
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsWithPhoto();

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * - показать объявления определенной марки.
     */
    @Test
    public void whenGetPostsBrandThenGetPostBrand() {
        Post expectedPost = getPost("post5");
        postRepository.create(expectedPost);

        Post post = getPost("post6");
        post.getCar().setBrand("Chery");
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsBrand("BMW");

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTablesAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
