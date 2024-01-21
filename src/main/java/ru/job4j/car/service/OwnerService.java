package ru.job4j.car.service;

import ru.job4j.car.model.Owner;
import ru.job4j.car.model.User;

import java.util.List;
import java.util.Optional;

public interface OwnerService {

    Owner create(User user);

    boolean update(Owner owner);

    boolean delete(Owner owner);

    List<Owner> findAll();

    Optional<Owner> findById(int ownerId);

    List<Owner> findByLikeName(String key);

    Optional<Owner> findByName(String name);

    Optional<Owner> findByUser(User user);
}
