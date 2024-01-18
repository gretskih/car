package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Brand;
import ru.job4j.car.repository.BrandRepository;
import ru.job4j.car.repository.BrandRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class BrandRepositoryTest {
    private final BrandRepository brandRepository = new BrandRepositoryImpl(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список Brands отсортированных по id
     */
    @Test
    public void whenFindAllBrandsThenGetBrandsList() {
        clearTableBefore();
        Brand expectedBrand1 = new Brand();
        expectedBrand1.setName("brand1");
        crudRepository.run(session -> session.persist(expectedBrand1));
        Brand expectedBrand2 = new Brand();
        expectedBrand2.setName("brand2");
        crudRepository.run(session -> session.persist(expectedBrand2));

        List<Brand> actualBrands = brandRepository.findAll();
        assertThat(actualBrands).usingRecursiveComparison().isEqualTo(List.of(expectedBrand1, expectedBrand2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindBrandByIdThenGetBrand() {
        Brand expectedBrand = new Brand();
        expectedBrand.setName("brand3");
        crudRepository.run(session -> session.persist(expectedBrand));

        Optional<Brand> actualBrand = brandRepository.findById(expectedBrand.getId());
        assertThat(actualBrand)
                .isPresent()
                .isNotEmpty()
                .contains(expectedBrand);
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTableAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
