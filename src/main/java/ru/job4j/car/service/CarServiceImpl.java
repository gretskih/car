package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.CarRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final EngineService engineService;
    private final OwnerService ownerService;
    private final PhotoService photoService;

    @Override
    public Car create(Car car, User user, Set<MultipartFile> files) throws Exception {
        try {
            setPhotos(car, files);
            setOwner(car, user);
            setPeriodHistory(car);
            setEngine(car);
            return carRepository.create(car);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            photoService.deleteAllPhotos(car.getPhotos());
            throw e;
        }
    }

    private void setPhotos(Car car, Set<MultipartFile> files) throws Exception {
        car.setPhotos(photoService.savePhotos(files));
    }

    private void setOwner(Car car, User user) {
        Owner owner = ownerService.create(user);
        if (owner == null) {
            throw new RuntimeException("новый владелец автомобиля не найден и не создан");
        }
        car.setOwner(owner);
        car.setHistoryOwners(Set.of(owner));
    }

    private void setPeriodHistory(Car car) {
        var periodHistory = PeriodHistory.of()
                .ownerId(car.getOwner().getId())
                .startAt(LocalDateTime.now())
                .build();
        car.setPeriodHistories(Set.of(periodHistory));
    }

    private void setEngine(Car car) {
        Optional<Engine> engineOptional = engineService.findById(car.getEngine().getId());
        if (engineOptional.isEmpty()) {
            throw new RuntimeException("двигатель не найден.");
        }
        car.setEngine(engineOptional.get());
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findByUser(User user) {
        return carRepository.findByUserId(user.getId());
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
