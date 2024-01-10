package ru.job4j.car.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CarService {

    List<Car> findAll();

    Car create(Car car, Set<MultipartFile> files);

    Optional<Car> findById(int carId);

    List<Car> findByUser(User user);

    boolean delete(int carId);
}
