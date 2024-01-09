package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Car;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepositoryImpl implements CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return автомобиль с id.
     */
    @Override
    public Car create(Car car) {
        if (crudRepository.run(session -> session.persist(car))) {
            return car;
        }
        return null;
    }

    /**
     * Обновить в базе автомобиль.
     * @param car автомобиль.
     */
    @Override
    public boolean update(Car car) {
        return crudRepository.run(session -> session.merge(car));
    }

    /**
     * Удалить автомобиль по id.
     * @param carId ID
     */
    @Override
    public boolean delete(int carId) {
        return crudRepository.run(
                "delete from Car where id = :fId",
                Map.of("fId", carId)
        );
    }

    /**
     * Список автомобилей отсортированных по id.
     * @return список автомобилей.
     */
    @Override
    public List<Car> findAllOrderById() {
        return crudRepository.query("from Car car "
                + "LEFT JOIN FETCH car.owners "
                + "LEFT JOIN FETCH car.photos order by car.id asc", Car.class);
    }

    /**
     * Найти автомобиль по ID
     * @return Optional or car.
     */
    @Override
    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "from Car car "
                        + "LEFT JOIN FETCH car.owners "
                        + "LEFT JOIN FETCH car.photos where car.id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }

    /**
     * Список автомобилей по name LIKE %key%
     * @param key key
     * @return список автомобилей.
     */
    @Override
    public List<Car> findByLikeName(String key) {
        return crudRepository.query(
                "from Car car "
                        + "LEFT JOIN FETCH car.owners "
                        + "LEFT JOIN FETCH car.photos where car.name like :fKey", Car.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти автомобиль по name.
     * @param name name.
     * @return Optional or car.
     */
    @Override
    public Optional<Car> findByName(String name) {
        return crudRepository.optional(
                "from Car car "
                        + "LEFT JOIN FETCH car.owners "
                        + "LEFT JOIN FETCH car.photos where car.name = :fName", Car.class,
                Map.of("fName", name)
        );
    }

    @Override
    public List<Car> findByUserId(int userId) {
        return crudRepository.query("from Car car "
                        + "where car.owner.ownerId = :fUserId "
                        + "order by car.id desc", Car.class,
                Map.of("fUserId", userId));
    }

    @Override
    public List<Car> findAll() {
        return crudRepository.query("from Car car "
                + "order by car.id asc", Car.class);
    }
}
