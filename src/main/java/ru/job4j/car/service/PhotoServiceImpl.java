package ru.job4j.car.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Photo;
import ru.job4j.car.repository.PhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
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
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Photo save(PhotoDto photoDto) throws IOException {
        var path = getNewFilePath(photoDto.getName());
        try {
            writeFileBytes(path, photoDto.getContent());
            Photo photo = new Photo();
            photo.setName(photoDto.getName());
            photo.setPath(path);
            return photo;
        } catch (Exception e) {
            deleteFile(path);
            throw e;
        }
    }

    @Override
    public Optional<PhotoDto> getFileById(int id) {
        var fileOptional = photoRepository.findById(id);
        if (fileOptional.isEmpty()) {
            return Optional.empty();
        }
        var content = readFileAsBytes(fileOptional.get().getPath());
        return Optional.of(new PhotoDto(fileOptional.get().getName(), content));
    }

    @Override
    public boolean deleteById(int id) {
        var photoOptional = photoRepository.findById(id);
        if (photoOptional.isPresent()) {
            deleteFile(photoOptional.get().getPath());
            return photoRepository.deleteById(id);
        }
        return false;
    }

    @Override
    public Set<Photo> savePhotos(Set<MultipartFile> files) throws IOException {
        Set<Photo> photos = new HashSet<>();
        try {
            for (MultipartFile file : files) {
                PhotoDto photoDto = new PhotoDto(file.getOriginalFilename(), file.getBytes());
                Photo photo = save(photoDto);
                photos.add(photo);
            }
            return photos;
        } catch (IOException e) {
            deleteAllPhotos(photos);
            throw e;
        }
    }

    @Override
    public void deleteAllPhotos(Set<Photo> photos) {
        photos.forEach(this::deleteByPhoto);
    }

    @Override
    public boolean deleteByPhoto(Photo photo) {
        return deleteFile(photo.getPath());
    }

    private boolean deleteFile(String path) {
        try {
            return Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new byte[]{};
    }

    private String getNewFilePath(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    private void writeFileBytes(String path, byte[] content) throws IOException {
            Files.write(Path.of(path), content);
    }
}
