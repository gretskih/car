package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Engine;
import ru.job4j.car.repository.EngineRepository;
import ru.job4j.car.repository.EngineRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class EngineRepositoryTest {

    private final EngineRepository engineRepository = new EngineRepositoryImpl(crudRepository);

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

        boolean actualStatusTransaction = engineRepository.update(expectedEngine);
        Optional<Engine> actualEngine = engineRepository.findById(expectedEngine.getId());

        assertThat(actualStatusTransaction).isTrue();
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

        boolean actualStatusTransaction = engineRepository.delete(expectedEngine);
        Optional<Engine> actualEngine = engineRepository.findById(expectedEngine.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualEngine).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllEnginesOrderByIdThenGetAllEngines() {
        clearTableBefore();
        Engine expectedEngine1 = new Engine();
        expectedEngine1.setName("engine4");
        engineRepository.create(expectedEngine1);
        Engine expectedEngine2 = new Engine();
        expectedEngine2.setName("engine5");
        engineRepository.create(expectedEngine2);

        List<Engine> actualEngines = engineRepository.findAll();
        assertThat(actualEngines).isEqualTo(List.of(expectedEngine2, expectedEngine1));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindEngineByIdThenGetEngine() {
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
    public void whenFindEngineByLikeNameThenGetEngine() {
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
    public void whenFindEngineByNameThenEngine() {
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
