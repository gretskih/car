package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Photo;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.CarRepository;
import ru.job4j.car.repository.OwnerRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final PhotoService photoService;
    private final OwnerRepository ownerRepository;

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findByUser(User user) {
        return carRepository.findByUser(user);
    }

    @Override
    public Car create(Car car, Set<MultipartFile> files) {
        Set<Photo> photos = new HashSet<>();
        try {
            for (MultipartFile file : files) {
                PhotoDto photoDto = new PhotoDto(file.getOriginalFilename(), file.getBytes());
                Photo photo = photoService.save(photoDto);
                photos.add(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        car.setPhotos(photos);
        return carRepository.create(car);
    }

    @Override
    public Optional<Car> findById(int carId) {
        return carRepository.findById(carId);
    }

    @Override
    public boolean delete(int carId) {
        var carOptional = carRepository.findById(carId);
        if (carOptional.isPresent()) {
            Set<Photo> photos = carOptional.get().getPhotos();
            int ownerId = carOptional.get().getOwner().getId();
            if (carRepository.delete(carId)) {
                photos.forEach(photoService::deleteByPhoto);
                ownerRepository.delete(ownerId);
                return true;
            }
        }
        return false;
    }
}
