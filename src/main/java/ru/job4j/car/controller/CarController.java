package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.*;
import ru.job4j.car.service.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final EngineService engineService;
    private final OwnerService ownerService;
    private final BrandService brandService;
    private final ColorService colorService;
    private final YearService yearService;
    private final BodyService bodyService;
    private final GearboxService gearboxService;
    private final FuelService fuelService;
    private final PhotoService photoService;

    @GetMapping
    public String getMyCarsPage(Model model, @SessionAttribute User user) {
        model.addAttribute("cars", carService.findByUser(user));
        return "cars/cars";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("engines", engineService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("years", yearService.findAll());
        model.addAttribute("bodies", bodyService.findAll());
        model.addAttribute("gearboxes", gearboxService.findAll());
        model.addAttribute("fuels", fuelService.findAll());
        return "cars/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Car car, @SessionAttribute User user, @RequestParam Set<MultipartFile> files, Model model) {
        car.setEngine(engineService.findById(car.getEngine().getId()).get());
        var ownerOptional = ownerService.findByUser(user);
        Owner owner;
        if (ownerOptional.isEmpty()) {
            owner = Owner.of()
                    .user(user)
                    .name(user.getName())
                    .build();
            ownerService.create(owner);
        } else {
            owner = ownerOptional.get();
        }
        car.setOwner(owner);
        car.setOwners(Set.of(owner));
        var periodHistory = PeriodHistory.of()
                .ownerId(owner.getId())
                .startAt(LocalDateTime.now()).build();
        car.setPeriodHistories(Set.of(periodHistory));

        Set<Photo> photos = new HashSet<>();
        try {
            for (MultipartFile file : files) {
                PhotoDto photoDto = new PhotoDto(file.getOriginalFilename(), file.getBytes());
                Photo photo = photoService.save(photoDto);
                photos.add(photo);
            }
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
        car.setPhotos(photos);

        if (carService.create(car) == null) {
            model.addAttribute("message", "Автомобиль не создан!");
            return "errors/404";
        }
        return "redirect:/posts/create";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int carId, Model model) {
        var carOptional = carService.findById(carId);
        if (carOptional.isPresent()) {
            Set<Photo> photos = carOptional.get().getPhotos();
            if (carService.delete(carOptional.get())) {
                photos.forEach(photoService::deleteByPhoto);
                return "redirect:/cars";
            }
        }
        model.addAttribute("message", "Произошла ошибка при удалении!");
        return "errors/404";
    }
}
