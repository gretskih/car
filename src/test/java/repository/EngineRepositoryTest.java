package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Engine;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineRepositoryTest {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private final EngineRepository engineRepository = new EngineRepository(new CrudRepository(sf));

    /**
     * Очистка базы
     */
    @BeforeEach
    public void clearTableEngine() {
        Session session = sf.openSession();
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

    /**
     * Обновление записи
     */
    @Test
    public void whenUpdateEngineThenGetUpdatedEngine() {
        Engine expectedEngine = new Engine();
        expectedEngine.setName("engine");
        engineRepository.create(expectedEngine);

        expectedEngine.setName("engine1");
        engineRepository.update(expectedEngine);
        Optional<Engine> actualEngine = engineRepository.findById(expectedEngine.getId());

        assertThat(actualEngine)
                .isPresent()
                .isNotEmpty()
                .contains(expectedEngine);
    }

    /**
     * Удаление записи
     */
    @Test
    public void whenDeleteEngineThenGetEmptyList() {
        Engine expectedEngine = new Engine();
        expectedEngine.setName("engine");
        engineRepository.create(expectedEngine);

        engineRepository.delete(expectedEngine.getId());
        List<Engine> actualEngines = engineRepository.findAllOrderById();

        assertThat(actualEngines).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenCreateNewEngineThenGetAllEngines() {
        Engine expectedEngine1 = new Engine();
        expectedEngine1.setName("engine1");
        engineRepository.create(expectedEngine1);
        Engine expectedEngine2 = new Engine();
        expectedEngine2.setName("engine2");
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
        expectedEngine.setName("engine");
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
        expectedEngine1.setName("engine");
        engineRepository.create(expectedEngine1);
        Engine expectedEngine2 = new Engine();
        expectedEngine2.setName("test");
        engineRepository.create(expectedEngine2);

        List<Engine> actualEngines = engineRepository.findByLikeName("es");
        assertThat(actualEngines).isEqualTo(List.of(expectedEngine2));
    }

    /**
     * Поиск по имени name
     */
    @Test
    public void whenCreateNewEngineThenGetByName() {
        Engine expectedEngine = new Engine();
        expectedEngine.setName("engine");
        engineRepository.create(expectedEngine);

        Optional<Engine> actualEngine = engineRepository.findByName(expectedEngine.getName());
        assertThat(actualEngine)
                .isPresent()
                .isNotEmpty()
                .contains(expectedEngine);
    }
}
