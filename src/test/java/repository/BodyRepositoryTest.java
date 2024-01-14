package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Body;
import ru.job4j.car.repository.BodyRepository;
import ru.job4j.car.repository.BodyRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class BodyRepositoryTest {
    private final BodyRepository bodyRepository = new BodyRepositoryImpl(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Body").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список Body отсортированных по типу
     */
    @Test
    public void whenFindAllBodyThenGetBodiesList() {
        clearTableBefore();
        Body expectedBody1 = new Body();
        expectedBody1.setBodyType("body1");
        bodyRepository.create(expectedBody1);
        Body expectedBody2 = new Body();
        expectedBody2.setBodyType("body2");
        bodyRepository.create(expectedBody2);

        List<Body> actualBodies = bodyRepository.findAllOrderByType();
        assertThat(actualBodies).usingRecursiveComparison().isEqualTo(List.of(expectedBody1, expectedBody2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindBodyByIdThenGetBody() {
        Body expectedBody = new Body();
        expectedBody.setBodyType("body3");
        bodyRepository.create(expectedBody);

        Optional<Body> actualBody = bodyRepository.findById(expectedBody.getId());
        assertThat(actualBody)
                .isPresent()
                .isNotEmpty()
                .contains(expectedBody);
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTableAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Body").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
