package ru.job4j.car.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Photo;
import ru.job4j.car.repository.PhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    private final String storageDirectory;

    public PhotoServiceImpl(PhotoRepository photoRepository,
                             @Value("${file.directory}") String storageDirectory) {
        this.photoRepository = photoRepository;
        this.storageDirectory = storageDirectory;
        createStorageDirectory(storageDirectory);
    }

    private void createStorageDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Photo save(PhotoDto photoDto) {
        var path = getNewFilePath(photoDto.getName());
        writeFileBytes(path, photoDto.getContent());
        Photo photo = new Photo();
        photo.setName(photoDto.getName());
        photo.setPath(path);
        return photo;
    }

    private String getNewFilePath(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    private void writeFileBytes(String path, byte[] content) {
        try {
            Files.write(Path.of(path), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
