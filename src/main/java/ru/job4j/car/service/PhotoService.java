package ru.job4j.car.service;

import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Photo;

public interface PhotoService {
    Photo save(PhotoDto fileDto);
/*
    Optional<PhotoDto> getFileById(int id);

    void deleteById(int id);

 */
}
