package ru.job4j.car.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.car.model.Photo;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранение фото
     * @param photo фото
     * @return фото
     */
    @Override
    public Photo save(Photo photo) {
        if (crudRepository.run(session -> session.persist(photo))) {
            return photo;
        }
        return null;
    }

    /**
     * Поиск фото по идентификатору
     * @param photoId идентификатор
     * @return optional фото
     */
    @Override
    public Optional<Photo> findById(int photoId) {
        return crudRepository.optional("from Photo where id = :fId", Photo.class,
                Map.of("fId", photoId)
                );
    }

    /**
     * Удаление фото по идентификатору
     * @param photoId идентификатор фото
     * @return true - удачно, false - неудачно
     */
    @Override
    public boolean deleteById(int photoId) {
        return crudRepository.run(
                "DELETE from Photo where id = :fId",
                Map.of("fId", photoId)
        );
    }
}
