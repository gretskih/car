package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Year;
import ru.job4j.car.repository.YearRepository;
import ru.job4j.car.repository.YearRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class YearRepositoryTest {
    private final YearRepository yearRepository = new YearRepositoryImpl(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Year").executeUpdate();
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
            session.createQuery("DELETE FROM Year").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список Years отсортированных по id
     */
    @Test
    public void whenFindAllYearsThenGetYearsList() {
        clearTableBefore();
        Year expectedYear1 = new Year();
        expectedYear1.setYear(2021);
        crudRepository.run(session -> session.persist(expectedYear1));
        Year expectedYear2 = new Year();
        expectedYear2.setYear(2020);
        crudRepository.run(session -> session.persist(expectedYear2));

        List<Year> actualYears = yearRepository.findAll();
        assertThat(actualYears).usingRecursiveComparison().isEqualTo(List.of(expectedYear2, expectedYear1));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindYearByIdThenGetYear() {
        Year expectedYear = new Year();
        expectedYear.setYear(2022);
        crudRepository.run(session -> session.persist(expectedYear));

        Optional<Year> actualYear = yearRepository.findById(expectedYear.getId());
        assertThat(actualYear)
                .isPresent()
                .isNotEmpty()
                .contains(expectedYear);
    }
}
