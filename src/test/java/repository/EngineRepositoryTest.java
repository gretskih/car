package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Engine;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.EngineRepository;
import ru.job4j.car.repository.EngineRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineRepositoryTest {

    private final EngineRepository engineRepository = new EngineRepositoryImpl(new CrudRepository(ConfigurationTest.sf));

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM PeriodHistory").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Обновление записи
     */
    @Test
    public void whenUpdateEngineThenGetUpdatedEngine() {
        Engine expectedEngine = new Engine();
        expectedEngine.setName("engine1");
        engineRepository.create(expectedEngine);

        expectedEngine.setName("engine2");
        engineRepository.update(expectedEngine);
        Optional<Engine> actualEngine = engineRepository.findById(expectedEngine.getId());

        assertThat(actualEngine)
                .isPresent()
                .isNotEmpty()
                .contains(expectedEngine);
    }

    /**
     * Создание/удаление записи
     */
    @Test
    public void whenDeleteEngineThenGetEmpty() {
        Engine expectedEngine = new Engine();
        expectedEngine.setName("engine3");
        engineRepository.create(expectedEngine);

        engineRepository.delete(expectedEngine.getId());
        Optional<Engine> actualEngine = engineRepository.findById(expectedEngine.getId());

        assertThat(actualEngine).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenCreateNewEngineThenGetAllEngines() {
        clearTableBefore();
        Engine expectedEngine1 = new Engine();
        expectedEngine1.setName("engine4");
        engineRepository.create(expectedEngine1);
        Engine expectedEngine2 = new Engine();
        expectedEngine2.setName("engine5");
        engineRepository.create(expectedEngine2);

        List<Engine> actualEngines = engineRepository.findAllOrderById();
        assertThat(actualEngines).isEqualTo(List.of(expectedEngine1, expectedEngine2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenCreateNewEngineThenGetEngineById() {
        Engine expectedEngine = new Engine();
        expectedEngine.setName("engine6");
        engineRepository.create(expectedEngine);

        Optional<Engine> actualEngine = engineRepository.findById(expectedEngine.getId());
        assertThat(actualEngine)
                .isPresent()
                .isNotEmpty()
                .contains(expectedEngine);
    }

    /**
     * Поиск по части имени
     */
    @Test
    public void whenCreateNewEngineThenGetByLikeName() {
        Engine expectedEngine1 = new Engine();
        expectedEngine1.setName("engine7");
        engineRepository.create(expectedEngine1);
        Engine expectedEngine2 = new Engine();
        expectedEngine2.setName("engine8");
        engineRepository.create(expectedEngine2);

        List<Engine> actualEngines = engineRepository.findByLikeName("ne8");
        assertThat(actualEngines).isEqualTo(List.of(expectedEngine2));
    }

    /**
     * Поиск по имени name
     */
    @Test
    public void whenCreateNewEngineThenGetByName() {
        Engine expectedEngine = new Engine();
        expectedEngine.setName("engine9");
        engineRepository.create(expectedEngine);

        Optional<Engine> actualEngine = engineRepository.findByName(expectedEngine.getName());
        assertThat(actualEngine)
                .isPresent()
                .isNotEmpty()
                .contains(expectedEngine);
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTableAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
