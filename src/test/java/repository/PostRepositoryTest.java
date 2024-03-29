package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.car.CarRepository;
import ru.job4j.car.repository.car.HibernateCarRepository;
import ru.job4j.car.repository.post.PostRepository;
import ru.job4j.car.repository.post.HibernatePostRepository;
import ru.job4j.car.repository.user.UserRepository;
import ru.job4j.car.repository.user.HibernateUserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.CarRepositoryTest.getCar;
import static repository.ConfigurationTest.crudRepository;

public class PostRepositoryTest {

    public static PostRepository postRepository = new HibernatePostRepository(crudRepository);
    public static UserRepository userRepository = new HibernateUserRepository(crudRepository);
    public static CarRepository carRepository = new HibernateCarRepository(crudRepository);

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
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Body").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.createQuery("DELETE FROM Gearbox").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM Year").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    static Post getPost(String postName) throws Exception {
        Car car = getCar("Car" + postName);
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
    public void whenFindPostsLastDayThenGetPostsLastDay() throws Exception {
        Post expectedPost = getPost("post1");
        postRepository.create(expectedPost);

        Post post = getPost("post2");
        post.setCreated(LocalDateTime.now().minusDays(2));
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsLastDay();

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * Показать все объявления
     */
    @Test
    public void whenFindPostsThenGetAllPosts() throws Exception {
        Post expectedPost1 = getPost("post3");
        postRepository.create(expectedPost1);

        Post expectedPost2 = getPost("post4");
        postRepository.create(expectedPost2);

        List<Post> actualPosts = postRepository.getPosts();

        assertThat(actualPosts).isEqualTo(List.of(expectedPost1, expectedPost2));
    }

    /**
     * - показать объявления определенной марки.
     */
    @Test
    public void whenFindPostsBrandThenGetPostBrand() throws Exception {
        Post expectedPost = getPost("post5");
        postRepository.create(expectedPost);

        Post post = getPost("post6");
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsBrand("brandCar" + "post5");

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * - показать объявления по идентификатору марки.
     */
    @Test
    public void whenFindPostsBrandIdThenGetPostBrand() throws Exception {
        Post expectedPost = getPost("post5");
        postRepository.create(expectedPost);

        Post post = getPost("post6");
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsBrandId(expectedPost.getCar().getBrand().getId());

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * - показать объявления от заданного пользователя.
     */
    @Test
    public void whenFindPostsUserThenGetPosts() throws Exception {
        Post expectedPost = getPost("post7");
        postRepository.create(expectedPost);

        Post post = getPost("post8");
        postRepository.create(post);

        List<Post> actualPosts = postRepository.getPostsUserId(expectedPost.getUser().getId());

        assertThat(actualPosts).isEqualTo(List.of(expectedPost));
    }

    /**
     * - найти по идентификатору
     */
    @Test
    public void whenFindPostByIdThenGetPost() throws Exception {
        Post expectedPost = getPost("post9");
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
    public void whenDeletePostThenGetPostEmpty() throws Exception {
        Post expectedPost = getPost("post10");
        postRepository.create(expectedPost);
        boolean actualStatusTransaction = postRepository.delete(expectedPost);

        var actualPost = postRepository.findById(expectedPost.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualPost).isEmpty();
    }

    /**
     * - установить статус объявлению
     */

    @Test
    public void whenSetTrueToPostThenGetPostStatusTrue() throws Exception {
        Post expectedPost = getPost("post11");
        postRepository.create(expectedPost);
        boolean actualStatusTransaction = postRepository.setStatus(expectedPost.getId(), true);

        var actualPost = postRepository.findById(expectedPost.getId());

        assertThat(actualStatusTransaction).isTrue();
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
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Body").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.createQuery("DELETE FROM Gearbox").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM Year").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
