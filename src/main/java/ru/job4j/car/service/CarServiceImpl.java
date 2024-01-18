package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car create(Car car) {
        try {
            return carRepository.create(car);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findByUser(User user) {
        return carRepository.findByUser(user);
    }

    @Override
    public Optional<Car> findById(int carId) {
        return carRepository.findById(carId);
    }

    @Override
    public boolean delete(Car car) {
        return carRepository.delete(car);
    }
}
