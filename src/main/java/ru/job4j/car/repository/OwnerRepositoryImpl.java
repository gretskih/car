package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerRepositoryImpl implements OwnerRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param owner владелец.
     * @return владелец с id.
     */
    @Override
    public Owner create(Owner owner) {
        if (crudRepository.run(session -> session.persist(owner))) {
            return owner;
        }
        return null;
    }

    /**
     * Обновить в базе владельца.
     * @param owner владелец.
     * @return статус транзакции
     */
    @Override
    public boolean update(Owner owner) {
        return crudRepository.run(session -> session.merge(owner));
    }

    /**
     * Удалить владельца по id.
     * @param owner ID
     * @return статус транзакции
     */
    @Override
    public boolean delete(Owner owner) {
        return crudRepository.run(session -> session.delete(owner));
    }

    /**
     * Список владельцев отсортированных по id.
     * @return список владельцев.
     */
    @Override
    public List<Owner> findAllOrderById() {
        return crudRepository.query("from Owner order by id asc", Owner.class);
    }

    /**
     * Найти владельца по ID
     * @return владелец.
     */
    @Override
    public Optional<Owner> findById(int ownerId) {
        return crudRepository.optional(
                "from Owner where id = :fId", Owner.class,
                Map.of("fId", ownerId)
        );
    }

    /**
     * Список владельцев по name LIKE %key%
     * @param key key
     * @return список владельцев.
     */
    @Override
    public List<Owner> findByLikeName(String key) {
        return crudRepository.query(
                "from Owner where name like :fKey", Owner.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти владельца по name.
     * @param name name.
     * @return Optional or owner.
     */
    @Override
    public Optional<Owner> findByName(String name) {
        return crudRepository.optional(
                "from Owner where name = :fName", Owner.class,
                Map.of("fName", name)
        );
    }

    /**
     * Найти владельца по User
     * @return Optional or owner.
     */
    @Override
    public Optional<Owner> findByUser(User user) {
        return crudRepository.optional(
                "from Owner where user = :fUser", Owner.class,
                Map.of("fUser", user)
        );
    }
}
