package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.model.*;
import ru.job4j.car.service.CarService;
import ru.job4j.car.service.PostService;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;
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

    @GetMapping("/day")
    public String getPageLastPosts(Model model) {
        var postPreviews = postService.getPostPreviewsLastDay();
        model.addAttribute("postPreviews", postPreviews);
        return "index";
    }

    @GetMapping("/photos")
    public String getPagePhotosPosts(Model model) {
        var postPreviews = postService.getPostPreviewsWithPhoto();
        model.addAttribute("postPreviews", postPreviews);
        return "index";
    }

    @GetMapping("/my")
    public String getPageMyPosts(Model model, @SessionAttribute User user) {
        var postPreviews = postService.getPostPreviewsUser(user);
        model.addAttribute("postPreviews", postPreviews);
        return "index";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, @SessionAttribute User user) {
        model.addAttribute("cars", carService.findByUser(user));
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Post post, @SessionAttribute User user, @RequestParam String price, @RequestParam Integer carId, Model model) {
        post.setCreated(LocalDateTime.now());
        post.setUser(user);
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(new BigInteger(price.replaceAll(" ", "")));
        priceHistory.setAfter(new BigInteger(price.replaceAll(" ", "")));
        priceHistory.setCreated(LocalDateTime.now());
        post.setPriceHistories(Set.of(priceHistory));

        Car car = carService.findById(carId).get();
        post.setCar(car);

        if (postService.create(post) == null) {
            model.addAttribute("message", "Объявление не создано!");
            return "errors/404";
        }
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String getViewPage(Model model, @PathVariable int id, @SessionAttribute User user) {
        Optional<PostView> postViewOptional = postService.findById(id);
        if (postViewOptional.isEmpty()) {
            model.addAttribute("message", "Объявление не найдено!");
            return "errors/404";
        }
        model.addAttribute("postView", postViewOptional.get());
        model.addAttribute("currentUserId", user.getId());
        return "posts/view";
    }

    @GetMapping("/complete/{id}")
    public String complete(@PathVariable int id, Model model) {
        if (!postService.setStatus(id, true)) {
            model.addAttribute("message", "Статус объявления не обновлен!");
            return "errors/404";
        }
        return "redirect:/posts";
    }

    @GetMapping("/place/{id}")
    public String place(@PathVariable int id, Model model) {
        if (!postService.setStatus(id, false)) {
            model.addAttribute("message", "Статус объявления не обновлен!");
            return "errors/404";
        }
        return "redirect:/posts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        if (!postService.delete(id)) {
            model.addAttribute("message", "Оъявление не было удалено!");
            return "errors/404";
        }
        return "redirect:/posts";
    }
}
