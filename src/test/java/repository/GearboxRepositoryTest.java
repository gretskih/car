package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Gearbox;
import ru.job4j.car.repository.GearboxRepository;
import ru.job4j.car.repository.GearboxRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class GearboxRepositoryTest {
    private final GearboxRepository gearboxRepository = new GearboxRepositoryImpl(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Gearbox").executeUpdate();
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
            session.createQuery("DELETE FROM Gearbox").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список Gearboxes отсортированных по id
     */
    @Test
    public void whenFindAllGearboxesThenGetGearboxesList() {
        clearTableBefore();
        Gearbox expectedGearbox1 = new Gearbox();
        expectedGearbox1.setType("gearbox1");
        gearboxRepository.create(expectedGearbox1);
        Gearbox expectedGearbox2 = new Gearbox();
        expectedGearbox2.setType("gearbox2");
        gearboxRepository.create(expectedGearbox2);

        List<Gearbox> actualGearboxes = gearboxRepository.findAll();
        assertThat(actualGearboxes).usingRecursiveComparison().isEqualTo(List.of(expectedGearbox1, expectedGearbox2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindGearboxByIdThenGetGearbox() {
        Gearbox expectedGearbox = new Gearbox();
        expectedGearbox.setType("gearbox3");
        gearboxRepository.create(expectedGearbox);

        Optional<Gearbox> actualGearbox = gearboxRepository.findById(expectedGearbox.getId());
        assertThat(actualGearbox)
                .isPresent()
                .isNotEmpty()
                .contains(expectedGearbox);
    }
    
}
