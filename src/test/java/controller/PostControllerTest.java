package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.car.controller.PostController;
import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.model.*;
import ru.job4j.car.service.BrandService;
import ru.job4j.car.service.CarService;
import ru.job4j.car.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @Mock
    private PostService postService;
    @Mock
    private CarService carService;
    @Mock
    private BrandService brandService;
    @InjectMocks
    private PostController postController;

    private User getUser() {
        return new User(1, "Name", "login", "pass", "1111");
    }

    private Car getCar() {
        return new Car(1, "Car", 1000, new Brand(), new Year(), new Body(),
                new Gearbox(), new Fuel(), new Color(), "New", new Engine(1, ""),
                new Owner(), Set.of(new Owner()), Set.of(new PeriodHistory()), Set.of(new Photo()));
    }

    private Post getPost() {
        return new Post(0, "Description", null, "City", false,
                null, null, null, null);
    }

    private PostPreview getPostPreview() {
        return new PostPreview(1, Set.of(new Photo(1, "Name", "Path")), "Photo",
                "Brand", 1000, 2015, "Body", "Gearbox", "Fuel",
                "Color", "Type", false, "Engine", "Owner", "1500000");
    }

    /**
     * Получение страницы index со списком всех объявлений
     */
    @Test
    public void whenRequestFindPostsThenGetAllPosts() {
        String expectedPage = "index";
        List<Brand> expectedBrands = List.of(new Brand(1, "Brand"));
        List<PostPreview> expectedPostPreviews = List.of(getPostPreview());
        when(postService.findAllPostPreview()).thenReturn(expectedPostPreviews);
        when(brandService.findAllOrderById()).thenReturn(expectedBrands);
        Model model = new ConcurrentModel();

        var actualPage = postController.getIndex(model);
        var actualPostPreviews = model.getAttribute("postPreviews");
        var actualBrands = model.getAttribute("brands");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(expectedPostPreviews);
        assertThat(actualBrands).usingRecursiveComparison().isEqualTo(expectedBrands);
    }

    /**
     * Получение страницы index со списком объявлений за последний день
     */
    @Test
    public void whenRequestFindPostsLastDayThenGetPosts() {
        String expectedPage = "index";
        List<PostPreview> expectedPostPreviews = List.of(getPostPreview());
        when(postService.getPostPreviewsLastDay()).thenReturn(expectedPostPreviews);
        Model model = new ConcurrentModel();

        var actualPage = postController.getPageLastPosts(model);
        var actualPostPreviews = model.getAttribute("postPreviews");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(expectedPostPreviews);
    }

    /**
     * Получение страницы index со списком объявлений бренда
     */
    @Captor
    private ArgumentCaptor<Integer> brandIdCaptor;

    @Test
    public void whenRequestFindPostsPostsOneBrandThenGetPosts() {
        String expectedPage = "index";
        int expectedId = 1;
        List<PostPreview> expectedPostPreviews = List.of(getPostPreview());
        List<Brand> expectedBrands = List.of(new Brand(expectedId, "Brand"));
        when(postService.getPostsPreviewsBrandId(brandIdCaptor.capture())).thenReturn(expectedPostPreviews);
        when(brandService.findAllOrderById()).thenReturn(expectedBrands);
        Model model = new ConcurrentModel();

        var actualPage = postController.getPagePostsOneBrand(expectedId, model);
        var actualPostPreviews = model.getAttribute("postPreviews");
        var actualBrands = model.getAttribute("brands");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(expectedPostPreviews);
        assertThat(actualBrands).usingRecursiveComparison().isEqualTo(expectedBrands);
        assertThat(brandIdCaptor.getValue()).isEqualTo(expectedId);
    }

    /**
     * Получение страницы index со списком объявлений с фото
     */
    @Test
    public void whenRequestFindPostsWithPhotoThenGetPostsWithPhoto() {
        String expectedPage = "index";
        List<PostPreview> expectedPostPreviews = List.of(getPostPreview());
        when(postService.getPostPreviewsWithPhoto()).thenReturn(expectedPostPreviews);
        Model model = new ConcurrentModel();

        var actualPage = postController.getPagePhotosPosts(model);
        var actualPostPreviews = model.getAttribute("postPreviews");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(expectedPostPreviews);
    }

    /**
     * Получение страницы index со списком объявлений от текущего пользователя user
     */
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void whenRequestFindMyPostsThenGetMyPosts() {
        String expectedPage = "index";
        List<PostPreview> expectedPostPreviews = List.of(getPostPreview());
        User user = getUser();
        when(postService.getPostPreviewsUser(userCaptor.capture())).thenReturn(expectedPostPreviews);
        Model model = new ConcurrentModel();

        var actualPage = postController.getPageMyPosts(model, user);
        var actualPostPreviews = model.getAttribute("postPreviews");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(expectedPostPreviews);
        assertThat(userCaptor.getValue()).usingRecursiveComparison().isEqualTo(user);
    }

    /**
     * Получение страницы posts/create со списком автомобилей текущего пользователя user
     */
    @Test
    public void whenRequestGetCreationPageThenGetPage() {
        String expectedPage = "posts/create";
        List<Car> expectedCars = List.of(getCar());
        User user = getUser();
        when(carService.findByUser(userCaptor.capture())).thenReturn(expectedCars);
        Model model = new ConcurrentModel();

        var actualPage = postController.getCreationPage(model, user);
        var actualCars = model.getAttribute("cars");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualCars).usingRecursiveComparison().isEqualTo(expectedCars);
        assertThat(userCaptor.getValue()).usingRecursiveComparison().isEqualTo(user);
    }

    /**
     * Сохранение нового объявления и редирект на страницу redirect:/posts
     */
    @Captor
    private ArgumentCaptor<Integer> carIdCaptor;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    @Test
    public void whenCreatePostThenGetPostsPage() {
        String expectedPage = "redirect:/posts";
        Post post = getPost();
        User expectedUser = getUser();
        String expectedPrice = "10000";
        Integer expectedCarId = 1;
        Car expectedCar = getCar();
        Model model = new ConcurrentModel();
        when(carService.findById(carIdCaptor.capture())).thenReturn(Optional.of(expectedCar));
        when(postService.create(postArgumentCaptor.capture())).thenReturn(post);

        String actualPage = postController.create(post, expectedUser, expectedPrice, expectedCarId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(post.getUser()).usingRecursiveComparison().isEqualTo(expectedUser);
        assertThat(post.getPrice()).isEqualTo(expectedPrice);
        assertThat(post.getCar()).usingRecursiveComparison().isEqualTo(expectedCar);
    }

    /**
     * Неудачное сохранение нового объявления и редирект на страницу errors/404
     */
    @Test
    public void whenCreatePostThenGetErrorPage() {
        String expectedPage = "errors/404";
        Post post = getPost();
        User expectedUser = getUser();
        String expectedPrice = "10000";
        Integer expectedCarId = 1;
        Car expectedCar = getCar();
        Model model = new ConcurrentModel();
        when(carService.findById(any(Integer.class))).thenReturn(Optional.of(expectedCar));
        when(postService.create(any())).thenReturn(null);

        String actualPage = postController.create(post, expectedUser, expectedPrice, expectedCarId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(model.getAttribute("message")).isEqualTo("Объявление не создано!");
    }

    /**
     * Получение страницы posts/view с подробной информацией об объявлении
     */
    @Captor
    private ArgumentCaptor<Integer> idCaptor;

    @Test
    public void whenRequestPostViewThenGetViewPage() {
        String expectedPage = "posts/view";
        User expectedUser = getUser();
        int expectedId = 2;
        PostView expectedPostView = new PostView(2, 1, Set.of(new Photo()), "Name",
                "Brand", 1000, 2020, "Body", "Gearbox", "Fuel",
                "Color", "Type", false, "Engine", "Owner", 2,
                "0000", "100000", "Description", LocalDateTime.now(), "City");
        when(postService.findById(idCaptor.capture())).thenReturn(Optional.of(expectedPostView));
        Model model = new ConcurrentModel();

        String actualPage = postController.getViewPage(model, expectedId, expectedUser);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(model.getAttribute("postView")).usingRecursiveComparison().isEqualTo(expectedPostView);
        assertThat(model.getAttribute("currentUserId")).isEqualTo(expectedUser.getId());
        assertThat(idCaptor.getValue()).isEqualTo(expectedId);
    }

    /**
     * Объявление по id не найдено, получение страницы errors/404
     */
    @Test
    public void whenRequestPostViewThenGetErrorPage() {
        String expectedPage = "errors/404";
        String expectedMessage = "Объявление не найдено!";
        User expectedUser = getUser();
        int expectedId = 2;
        PostView expectedPostView = new PostView(2, 1, Set.of(new Photo()), "Name",
                "Brand", 1000, 2020, "Body", "Gearbox", "Fuel",
                "Color", "Type", false, "Engine", "Owner", 2,
                "0000", "100000", "Description", LocalDateTime.now(), "City");
        when(postService.findById(any(Integer.class))).thenReturn(Optional.empty());
        Model model = new ConcurrentModel();

        String actualPage = postController.getViewPage(model, expectedId, expectedUser);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(model.getAttribute("postView")).isNull();
        assertThat(model.getAttribute("currentUserId")).isNull();
        assertThat(model.getAttribute("message")).isEqualTo(expectedMessage);
    }

    /**
     * Объявление завершено, редирект на страницу redirect:/posts
     */
    @Captor
    private ArgumentCaptor<Boolean> statusCaptor;

    @Test
    public void whenRequestPostCompleteThenGetPostsPage() {
        String expectedPage = "redirect:/posts";
        int expectedId = 2;
        when(postService.setStatus(idCaptor.capture(), statusCaptor.capture())).thenReturn(true);
        Model model = new ConcurrentModel();

        String actualPage = postController.complete(expectedId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(idCaptor.getValue()).isEqualTo(expectedId);
        assertThat(statusCaptor.getValue()).isTrue();
    }

    /**
     * Статус объявления не изменен, получение страницы errors/404
     */
    @Test
    public void whenRequestPostCompleteThenGetErrorPage() {
        String expectedPage = "errors/404";
        String expectedMessage = "Статус объявления не обновлен!";
        int expectedId = 2;
        when(postService.setStatus(any(Integer.class), any(Boolean.class))).thenReturn(false);
        Model model = new ConcurrentModel();

        String actualPage = postController.complete(expectedId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(model.getAttribute("message")).isEqualTo(expectedMessage);
    }

    /**
     * Объявление повторно размещено, редирект на страницу redirect:/posts
     */
    @Test
    public void whenRequestPostPlaceThenGetPostsPage() {
        String expectedPage = "redirect:/posts";
        int expectedId = 2;
        when(postService.setStatus(idCaptor.capture(), statusCaptor.capture())).thenReturn(true);
        Model model = new ConcurrentModel();

        String actualPage = postController.place(expectedId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(idCaptor.getValue()).isEqualTo(expectedId);
        assertThat(statusCaptor.getValue()).isFalse();
    }

    /**
     * Объявление повторно не размещено, получение страницы errors/404
     */
    @Test
    public void whenRequestPostPlaceThenGetErrorPage() {
        String expectedPage = "errors/404";
        String expectedMessage = "Статус объявления не обновлен!";
        int expectedId = 2;
        when(postService.setStatus(any(Integer.class), any(Boolean.class))).thenReturn(false);
        Model model = new ConcurrentModel();

        String actualPage = postController.place(expectedId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(model.getAttribute("message")).isEqualTo(expectedMessage);
    }

    /**
     * Удаление объявления и редирект на страницу redirect:/posts
     */
    @Test
    public void whenRequestDeletePostThenGetPostsPage() {
        String expectedPage = "redirect:/posts";
        int expectedId = 2;
        when(postService.delete(idCaptor.capture())).thenReturn(true);
        Model model = new ConcurrentModel();

        String actualPage = postController.delete(expectedId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(idCaptor.getValue()).isEqualTo(expectedId);
    }

    /**
     * Неудачное удаление объявления и получение страницы errors/404
     */
    @Test
    public void whenRequestDeletePostThenGetErrorPage() {
        String expectedPage = "errors/404";
        String expectedMessage = "Оъявление не было удалено!";
        int expectedId = 2;
        when(postService.delete(any(Integer.class))).thenReturn(false);
        Model model = new ConcurrentModel();

        String actualPage = postController.delete(expectedId, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(model.getAttribute("message")).isEqualTo(expectedMessage);
    }
}
