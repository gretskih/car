package ru.job4j.car.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Photo;

import java.util.Optional;
import java.util.Set;

public interface PhotoService {
    Photo save(PhotoDto fileDto) throws Exception;

    Optional<PhotoDto> getFileById(int id);

    boolean deleteByPhoto(Photo photo);

    boolean deleteById(int id);

    Set<Photo> savePhotos(Set<MultipartFile> files) throws Exception;

    void deleteAllPhotos(Set<Photo> photos);
}
