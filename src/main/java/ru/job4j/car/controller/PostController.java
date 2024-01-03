package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.PriceHistory;
import ru.job4j.car.model.User;
import ru.job4j.car.service.CarService;
import ru.job4j.car.service.PostService;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CarService carService;

    @GetMapping
    public String getIndex(Model model) {
        var postPreviews = postService.findAllPostPreview();
        model.addAttribute("postPreviews", postPreviews);
        return "index";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("cars", carService.findAll());
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Post post, @SessionAttribute User user, @RequestParam String price, @RequestParam Integer carId) {
        System.out.println(post);
        System.out.println(user);
        System.out.println(price);
        System.out.println(carId);

        post.setCreated(LocalDateTime.now());

        post.setUser(user);

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(new BigInteger(price.replaceAll(" ", "")));
        priceHistory.setAfter(new BigInteger(price.replaceAll(" ", "")));
        priceHistory.setCreated(LocalDateTime.now());
        post.setPriceHistories(Set.of(priceHistory));

        Car car = carService.findById(carId).get();
        post.setCar(car);

        postService.create(post);
        return "redirect:/posts";
    }
}
