package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostRepositoryTest {

    private final PostRepository postRepository = new PostRepository(ConfigurationTest.crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTablesBefore() {
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

    @Test
    public void test1() {
        Post expectedPost = getPost("post1");
        postRepository.create(expectedPost);
        System.out.println(expectedPost);

        Post post = getPost("post2");
        post.setCreated(LocalDateTime.now().minusDays(2));
        postRepository.create(post);
        System.out.println(post);

        //List<Post> actualPosts = postRepository.getPostsWithPhoto();
        //assertThat(actualPosts).isEqualTo(List.of(expectedPost));
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
