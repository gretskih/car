package ru.job4j.car.repository.photo;

import ru.job4j.car.model.Photo;

import java.util.Optional;

public interface PhotoRepository {

    /**
     * Сохранение фото
     * @param photo фото
     * @return фото
     */
    Photo save(Photo photo);

    /**
     * Поиск фото по идентификатору
     * @param photoId идентификатор
     * @return optional фото
     */
    Optional<Photo> findById(int photoId);

    /**
     * Удаление фото по идентификатору
     * @param photoId идентификатор фото
     * @return true - удачно, false - неудачно
     */
    boolean deleteById(int photoId);
}
