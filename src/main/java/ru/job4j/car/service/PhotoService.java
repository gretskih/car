package ru.job4j.car.service;

import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Photo;

import java.util.Optional;

public interface PhotoService {
    Photo save(PhotoDto fileDto);

    Optional<PhotoDto> getFileById(int id);

    void deleteByPhoto(Photo photo);

    boolean deleteById(int id);
}
