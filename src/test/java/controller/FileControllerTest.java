package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.controller.FileController;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.service.PhotoService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {
    @Mock
    private PhotoService photoService;
    @InjectMocks
    private FileController fileController;

    /**
     * Запрос содержимого файла по id существующего объекта File
     * @throws Exception
     */
    @Captor
    ArgumentCaptor<Integer> fileIdCaptor;

    @Test
    public void whenRequestFileByIdThenGetResponseEntityOk() throws Exception {
        int expectedId = 1;
        MultipartFile testFile = new MockMultipartFile("testFile.img", new byte[] {1, 2, 3});
        var fileDto = new PhotoDto(testFile.getOriginalFilename(), testFile.getBytes());
        when(photoService.getFileById(fileIdCaptor.capture())).thenReturn(Optional.of(fileDto));

        var response = fileController.getById(expectedId);
        var actualId = fileIdCaptor.getValue();

        assertThat(expectedId).isEqualTo(actualId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(fileDto.getContent());
    }

    /**
     * Запрос содержимого файла по id не существующего объекта File
     * @throws Exception
     */
    @Test
    public void whenRequestFileByIdThenGetResponseNotFound() throws Exception {
        int expectedId = 1;
        when(photoService.getFileById(any(Integer.class))).thenReturn(Optional.empty());

        var response = fileController.getById(expectedId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}
