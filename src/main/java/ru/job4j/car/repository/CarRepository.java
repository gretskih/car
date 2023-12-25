package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import ru.job4j.car.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return автомобиль с id.
     */
    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    /**
     * Обновить в базе автомобиль.
     * @param car автомобиль.
     */
    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    /**
     * Удалить автомобиль по id.
     * @param carId ID
     */
    public void delete(int carId) {
        crudRepository.run(
                "delete from Car where id = :fId",
                Map.of("fId", carId)
        );
    }

    /**
     * Список автомобилей отсортированных по id.
     * @return список автомобилей.
     */
    public List<Car> findAllOrderById() {
        return crudRepository.query("from Car car JOIN FETCH car.owners order by id asc", Car.class);
    }

    /**
     * Найти автомобиль по ID
     * @return автомобиль.
     */
    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "from Car car JOIN FETCH car.owners where id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }

    /**
     * Список автомобилей по name LIKE %key%
     * @param key key
     * @return список автомобилей.
     */
    public List<Car> findByLikeName(String key) {
        return crudRepository.query(
                "from Car car JOIN FETCH car.owners where name like :fKey", Car.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти автомобиль по name.
     * @param name name.
     * @return Optional or car.
     */
    public Optional<Car> findByName(String name) {
        return crudRepository.optional(
                "from Car car JOIN FETCH car.owners where name = :fName", Car.class,
                Map.of("fName", name)
        );
    }
}
