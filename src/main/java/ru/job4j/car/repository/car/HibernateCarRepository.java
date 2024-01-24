package ru.job4j.car.repository.car;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Car;
import ru.job4j.car.repository.CrudRepository;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateCarRepository implements CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return автомобиль с id.
     */
    @Override
    public Car create(Car car) throws Exception {
        if (!crudRepository.run(session -> session.persist(car))) {
            throw new Exception("ошибка при сохранении автомобиля.");
        }
        return car;
    }

    /**
     * Обновить в базе автомобиль.
     * @param car автомобиль.
     * @return статус транзакции
     */
    @Override
    public boolean update(Car car) {
        return crudRepository.run(session -> session.merge(car));
    }

    /**
     * Удалить автомобиль по id.
     * @param car ID
     * @return статус транзакции
     */
    @Override
    public boolean delete(Car car) {
        return crudRepository.run(session -> session.delete(car));
    }

    /**
     * Список автомобилей отсортированных по id.
     * @return список автомобилей.
     */
    @Override
    public List<Car> findAll() {
        return crudRepository.query("from Car car "
                + "LEFT JOIN FETCH car.historyOwners "
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
                        + "LEFT JOIN FETCH car.historyOwners "
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
                        + "LEFT JOIN FETCH car.historyOwners "
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
                        + "LEFT JOIN FETCH car.historyOwners "
                        + "LEFT JOIN FETCH car.photos where car.name = :fName", Car.class,
                Map.of("fName", name)
        );
    }

    /**
     * Найти все автомобили пользователя
     * @param userId идентификатор пользователя
     * @return список автомобилей
     */
    @Override
    public List<Car> findByUserId(int userId) {
        return crudRepository.query("from Car car "
                        + "LEFT JOIN FETCH car.brand "
                        + "where car.owner.user.id = :fUserId "
                        + "order by car.id desc", Car.class,
                Map.of("fUserId", userId));
    }
}
