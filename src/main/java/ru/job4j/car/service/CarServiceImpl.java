package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.dto.CarPreview;
import ru.job4j.car.mappers.CarPreviewMapper;
import ru.job4j.car.model.Car;
import ru.job4j.car.repository.CarRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarPreviewMapper carPreviewMapper;

    @Override
    public List<CarPreview> findAll() {
        List<Car> cars = carRepository.findAllOrderById();
        if(cars.isEmpty()) {
            return Collections.emptyList();
        }
        List<CarPreview> carPreviews = new ArrayList<>();
        for(Car car: cars) {
            carPreviews.add(carPreviewMapper.getCarPreview(car));
        }
        return carPreviews;
    }

    @Override
    public Car create(Car car) {
        return carRepository.create(car);
    }
}
