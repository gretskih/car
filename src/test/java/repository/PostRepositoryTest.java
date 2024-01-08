package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostRepositoryTest {

    private final PostRepository postRepository = new PostRepositoryImpl(ConfigurationTest.crudRepository);
    public static UserRepository userRepository = new UserRepositoryImpl(new CrudRepository(ConfigurationTest.sf));
    public static CarRepository carRepository = new CarRepositoryImpl(ConfigurationTest.crudRepository);

    /**
     * Очистка базы
     */
    @BeforeEach
    public void clearTablesBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM PriceHistory").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM PeriodHistory").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    static Post getPost(String postName, String carBrand) {
        Car car = CarRepositoryTest.getCar("Car" + postName);
        car.setBrand(carBrand);
        carRepository.create(car);

        User user = new User();
        user.setLogin("user" + postName);
        user.setName("name" + postName);
        user.setPassword("0000");
        user.setContact("user@user.ru");
        userRepository.create(user);

        Post post = new Post();
        post.setDescription("description");
        post.setCreated(LocalDateTime.now());
        post.setCity("Москва");
        post.setUser(user);
        post.setCar(car);

        return post;
    }

    /**
     * - показать объявления за последний день;
     */
    @Test
    public void whenGetPostsLastDayThenGetPostLastDay() {
        Post expectedPost = getPost("post1", "BMW");
        postRepository.create(expectedPost);

        Post post = getPost("post2", "Toyota");
        post.setCreated(LocalDateTime.now().minusDays(2));
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsLastDay();

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * Показать все объявления
     */
    @Test
    public void whenGetPostsThenGetAllPosts() {
        Post expectedPost1 = getPost("post3", "BMW");
        postRepository.create(expectedPost1);

        Post expectedPost2 = getPost("post4", "Toyota");
        postRepository.create(expectedPost2);

        List<Post> actualPosts = postRepository.getPostsLastDay();

        assertThat(actualPosts).isEqualTo(List.of(expectedPost1, expectedPost2));
    }

    /**
     * - показать объявления определенной марки.
     */
    @Test
    public void whenGetPostsBrandThenGetPostBrand() {
        Post expectedPost = getPost("post5", "BMW");
        postRepository.create(expectedPost);

        Post post = getPost("post6", "Nissan");
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsBrand("BMW");

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * - показать объявления от заданного пользователя.
     */
    @Test
    public void whenGetPostsUserThenGetPagePosts() {
        Post expectedPost = getPost("post7", "BMW");
        postRepository.create(expectedPost);

        Post post = getPost("post8", "Nissan");
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsUser(expectedPost.getUser());

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * - найти по идентификатору
     */
    @Test
    public void whenCreateNewPostThenGetPostById() {
        Post expectedPost = getPost("post9", "BMW");
        postRepository.create(expectedPost);

        var actualPost = postRepository.findById(expectedPost.getId());

        assertThat(actualPost)
                .isPresent()
                .isNotEmpty()
                .contains(expectedPost);
    }

    /**
     * - удалить объявление по идентификатору
     */

    @Test
    public void whenDeletePostThenGetPostByIdIsEmpty() {
        Post expectedPost = getPost("post10", "BMW");
        postRepository.create(expectedPost);
        postRepository.delete(expectedPost.getId());

        var actualPost = postRepository.findById(expectedPost.getId());

        assertThat(actualPost).isEmpty();
    }

    /**
     * - установить статус объявлению
     */

    @Test
    public void whenSetTrueToPostThenGetPostStatusTrue() {
        Post expectedPost = getPost("post11", "BMW");
        postRepository.create(expectedPost);
        postRepository.setStatus(expectedPost.getId(), true);

        var actualPost = postRepository.findById(expectedPost.getId());

        assertThat(actualPost.get().isStatus())
                .isTrue();
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
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM PriceHistory").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM PeriodHistory").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
