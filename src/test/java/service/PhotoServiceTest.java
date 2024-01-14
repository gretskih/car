package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Photo;
import ru.job4j.car.repository.PhotoRepository;
import ru.job4j.car.service.PhotoServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PhotoServiceTest {
    private PhotoRepository photoRepository;
    private PhotoServiceImpl photoService;

    @BeforeEach
    public void initServices() {
        photoRepository = mock(PhotoRepository.class);
        photoService = new PhotoServiceImpl(photoRepository, "files");
    }

    /**
     * Сохранение фото в папке files
     * @throws IOException
     */
    @Test
    public void whenSavePhotoDtoThenGetPhoto() throws IOException {
        PhotoDto expectedPhotoDto = new PhotoDto("TestPhoto", new byte[]{1, 2, 3});

        Photo actualPhoto = photoService.save(expectedPhotoDto);
        var actualContent = Files.readAllBytes(Path.of(actualPhoto.getPath()));
        Files.deleteIfExists(Path.of(actualPhoto.getPath()));

        assertThat(actualPhoto.getName()).isEqualTo(expectedPhotoDto.getName());
        assertThat(actualContent).isEqualTo(expectedPhotoDto.getContent());
    }

    /**
     * Получение файла фото по id
     * @throws IOException
     */
    @Test
    public void whenGetPhotoByIdThenGetPhotoDto() throws IOException {
        PhotoDto expectedPhotoDto = new PhotoDto("TestPhoto", new byte[]{1, 2, 3});
        Photo expectedPhoto = photoService.save(expectedPhotoDto);
        expectedPhoto.setId(1);
        var idPhotoCaptor = ArgumentCaptor.forClass(int.class);
        when(photoRepository.findById(idPhotoCaptor.capture())).thenReturn(Optional.of(expectedPhoto));

        var actualPhotoDto = photoService.getFileById(expectedPhoto.getId());
        Files.deleteIfExists(Path.of(expectedPhoto.getPath()));

        assertThat(actualPhotoDto.get()).usingRecursiveComparison().isEqualTo(expectedPhotoDto);
        assertThat(idPhotoCaptor.getValue()).isEqualTo(expectedPhoto.getId());
    }

    /**
     * Получение файла фото по id
     * Фото отсутствует
     * @throws IOException
     */
    @Test
    public void whenGetPhotoByIdThenGetEmpty() {
        when(photoRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        var actualPhotoDto = photoService.getFileById(1);

        assertThat(actualPhotoDto).isEmpty();
    }

    /**
     * Удаление файла фото по id
     * @throws IOException
     */
    @Test
    public void whenDeletePhotoByIdThenGetTrue() throws IOException {
        PhotoDto expectedPhotoDto = new PhotoDto("TestPhoto", new byte[]{1, 2, 3});
        Photo expectedPhoto = photoService.save(expectedPhotoDto);
        expectedPhoto.setId(1);
        var idPhotoCaptor = ArgumentCaptor.forClass(int.class);
        when(photoRepository.findById(idPhotoCaptor.capture())).thenReturn(Optional.of(expectedPhoto));
        when(photoRepository.deleteById(idPhotoCaptor.capture())).thenReturn(true);

        var actualStatus1 = photoService.deleteById(expectedPhoto.getId());
        var actualStatus2 = Files.exists(Path.of(expectedPhoto.getPath()));
        Files.deleteIfExists(Path.of(expectedPhoto.getPath()));

        assertThat(actualStatus1).isTrue();
        assertThat(actualStatus2).isFalse();
        assertThat(idPhotoCaptor.getAllValues()).isEqualTo(List.of(expectedPhoto.getId(), (expectedPhoto.getId())));
    }

    /**
     * Удаление файла фото по id
     * Фото не существует
     * @throws IOException
     */
    @Test
    public void whenDeletePhotoByIdThenGetFalse() {
        when(photoRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        var actualStatus = photoService.deleteById(1);

        assertThat(actualStatus).isFalse();
    }

    /**
     * Удаление файла фото по Photo
     * @throws IOException
     */
    @Test
    public void whenDeletePhotoThenPhotoIsAbsent() throws IOException {
        PhotoDto expectedPhotoDto = new PhotoDto("TestPhoto", new byte[]{1, 2, 3});
        Photo expectedPhoto = photoService.save(expectedPhotoDto);

        var actualStatus1 = Files.exists(Path.of(expectedPhoto.getPath()));
        var actualStatus2 = photoService.deleteByPhoto(expectedPhoto);
        var actualStatus3 = Files.exists(Path.of(expectedPhoto.getPath()));
        Files.deleteIfExists(Path.of(expectedPhoto.getPath()));

        assertThat(actualStatus1).isTrue();
        assertThat(actualStatus2).isTrue();
        assertThat(actualStatus3).isFalse();
    }
}
