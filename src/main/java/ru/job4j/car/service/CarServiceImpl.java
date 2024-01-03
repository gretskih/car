package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Photo;
import ru.job4j.car.repository.CarRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final PhotoService photoService;

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car create(Car car, Set<MultipartFile> files) throws IOException {
        Set<Photo> photos = new HashSet<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = new PhotoDto(file.getOriginalFilename(), file.getBytes());
            Photo photo = photoService.save(photoDto);
            photos.add(photo);
        }
        car.setPhotos(photos);
        return carRepository.create(car);
    }

    @Override
    public Optional<Car> findById(int carId) {
        return carRepository.findById(carId);
    }
}
