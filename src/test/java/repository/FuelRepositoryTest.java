package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Fuel;
import ru.job4j.car.repository.FuelRepository;
import ru.job4j.car.repository.FuelRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class FuelRepositoryTest {
    private final FuelRepository fuelRepository = new FuelRepositoryImpl(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
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
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список Fuels отсортированных по id
     */
    @Test
    public void whenFindAllFuelsThenGetFuelsList() {
        clearTableBefore();
        Fuel expectedFuel1 = new Fuel();
        expectedFuel1.setType("fuel1");
        fuelRepository.create(expectedFuel1);
        Fuel expectedFuel2 = new Fuel();
        expectedFuel2.setType("fuel2");
        fuelRepository.create(expectedFuel2);

        List<Fuel> actualFuels = fuelRepository.findAllOrderById();
        assertThat(actualFuels).usingRecursiveComparison().isEqualTo(List.of(expectedFuel1, expectedFuel2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindFuelByIdThenGetFuel() {
        Fuel expectedFuel = new Fuel();
        expectedFuel.setType("fuel3");
        fuelRepository.create(expectedFuel);

        Optional<Fuel> actualFuel = fuelRepository.findById(expectedFuel.getId());
        assertThat(actualFuel)
                .isPresent()
                .isNotEmpty()
                .contains(expectedFuel);
    }
}
