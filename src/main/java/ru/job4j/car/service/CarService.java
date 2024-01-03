package ru.job4j.car.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.model.Car;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CarService {

    List<Car> findAll();

    Car create(Car car, Set<MultipartFile> files) throws IOException;

    Optional<Car> findById(int carId);
}
