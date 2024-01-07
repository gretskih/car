package ru.job4j.car.repository;

import ru.job4j.car.model.Photo;

import java.util.Optional;

public interface PhotoRepository {

    Photo save(Photo file);

    Optional<Photo> findById(int id);

    void deleteById(int id);
}
