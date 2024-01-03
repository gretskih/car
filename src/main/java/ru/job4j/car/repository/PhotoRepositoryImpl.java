package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Photo;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository {

    private final CrudRepository crudRepository;

    @Override
    public Photo save(Photo photo) {
        crudRepository.run(session -> session.persist(photo));
        return photo;
    }
/*
    @Override
    public Optional<Photo> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(int id) {

    }

 */
}
