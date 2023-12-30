package ru.job4j.car.service;

import ru.job4j.car.dto.CarPreview;
import ru.job4j.car.model.Car;

import java.util.List;

public interface CarService {

    List<CarPreview> findAll();

    Car create(Car car);
}
