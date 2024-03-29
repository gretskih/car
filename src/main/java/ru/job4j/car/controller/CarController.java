package ru.job4j.car.controller;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.model.*;
import ru.job4j.car.service.body.BodyService;
import ru.job4j.car.service.brand.BrandService;
import ru.job4j.car.service.car.CarService;
import ru.job4j.car.service.color.ColorService;
import ru.job4j.car.service.engine.EngineService;
import ru.job4j.car.service.fuel.FuelService;
import ru.job4j.car.service.gearbox.GearboxService;
import ru.job4j.car.service.photo.PhotoService;
import ru.job4j.car.service.year.YearService;

import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final EngineService engineService;
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
        try {
            carService.create(car, user, files);
        } catch (Exception e) {
            model.addAttribute("message1", "Автомобиль не добавлен.");
            model.addAttribute("message2", "Подробности: " + e.getMessage());
            return "errors/500";
        }
        return "redirect:/posts/create";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int carId, Model model) {
        var carOptional = carService.findById(carId);
        if (carOptional.isPresent()) {
            Set<Photo> photos = carOptional.get().getPhotos();
            if (carService.delete(carOptional.get())) {
                photoService.deleteAllPhotos(photos);
                return "redirect:/cars";
            }
        }
        model.addAttribute("message1", "Произошла ошибка при удалении.");
        return "errors/500";
    }
}