package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Color;
import ru.job4j.car.repository.color.ColorRepository;
import ru.job4j.car.repository.color.HibernateColorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class ColorRepositoryTest {
    private final ColorRepository colorRepository = new HibernateColorRepository(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTableAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список Colors отсортированных по id
     */
    @Test
    public void whenFindAllColorsThenGetColorsList() {
        clearTableBefore();
        Color expectedColor1 = new Color();
        expectedColor1.setName("color1");
        crudRepository.run(session -> session.persist(expectedColor1));
        Color expectedColor2 = new Color();
        expectedColor2.setName("color2");
        crudRepository.run(session -> session.persist(expectedColor2));

        List<Color> actualColors = colorRepository.findAll();
        assertThat(actualColors).usingRecursiveComparison().isEqualTo(List.of(expectedColor1, expectedColor2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindColorByIdThenGetColor() {
        Color expectedColor = new Color();
        expectedColor.setName("color3");
        crudRepository.run(session -> session.persist(expectedColor));

        Optional<Color> actualColor = colorRepository.findById(expectedColor.getId());
        assertThat(actualColor)
                .isPresent()
                .isNotEmpty()
                .contains(expectedColor);
    }
}
