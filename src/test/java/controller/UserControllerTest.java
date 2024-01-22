package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.car.controller.UserController;
import ru.job4j.car.model.User;
import ru.job4j.car.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    @Captor
    private ArgumentCaptor<String> loginArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> passwordArgumentCaptor;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User getUser() {
        return new User(1, "Name", "login", "pass", "1111");
    }

    /**
     * Получение страницы users/register
     */
    @Test
    public void whenRequestRegistrationThenGetRegistrationPage() {
        String expectedPage = "users/register";
        var actualPage = userController.getRegistrationPage();
        assertThat(actualPage).isEqualTo(expectedPage);
    }

    /**
     * Создание нового пользователя и перенаправление на страницу /users/login.
     */

    @Test
    public void whenPostUserRegisterThenGetLoginPage() {
        String expectedPage = "redirect:/users/login";
        User expectedUser = getUser();
        when(userService.create(userArgumentCaptor.capture())).thenReturn(Optional.of(expectedUser));
        var model = new ConcurrentModel();

        var actualPage = userController.register(expectedUser, model);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    /**
     * Создание уже существующего пользователя и перенаправление на страницу users/register.
     */

    @Test
    public void whenPostExistUserRegisterThenGetRegisterPage() {
        String expectedPage = "users/register";
        User expectedUser = getUser();
        String expectedMessage = String.format("Пользователь с логином %s уже существует.".formatted(expectedUser.getLogin()));
        when(userService.create(any())).thenReturn(Optional.empty());
        var model = new ConcurrentModel();

        var actualPage = userController.register(expectedUser, model);
        var actualMessage = model.getAttribute("error");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    /**
     * Получение страницы users/login
     */
    @Test
    public void whenRequestLoginThenGetLoginPage() {
        String expectedPage = "users/login";
        var actualPage = userController.getLoginPage();
        assertThat(actualPage).isEqualTo(expectedPage);
    }

    /**
     * Валидация существующего пользователя, сохранение пользователя в сессии, перенаправление на страницу /posts.
     */
    @Test
    public void whenPostLoginUserThenGetVacanciesPage() {
        String expectedPage = "redirect:/posts";
        User expectedUser = getUser();
        HttpServletRequest request = new MockHttpServletRequest();
        when(userService.findByLoginAndPassword(loginArgumentCaptor.capture(), passwordArgumentCaptor.capture())).thenReturn(Optional.of(expectedUser));
        var model = new ConcurrentModel();

        var actualPage = userController.loginUser(expectedUser, model, request);
        var actualLogin = loginArgumentCaptor.getValue();
        var actualPassword = passwordArgumentCaptor.getValue();
        var actualUser = request.getSession().getAttribute("user");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualLogin).isEqualTo(expectedUser.getLogin());
        assertThat(actualPassword).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    /**
     * Валидация отсутствующего пользователя, перенаправление на страницу users/login.
     */
    @Test
    public void whenPostErrorLoginUserThenGetLoginsPage() {
        String expectedPage = "users/login";
        String expectedMessage = "Логин или пароль введены неверно";
        User expectedUser = getUser();
        HttpServletRequest request = new MockHttpServletRequest();
        when(userService.findByLoginAndPassword(any(), any()))
                .thenReturn(Optional.empty());
        var model = new ConcurrentModel();

        var actualPage = userController.loginUser(expectedUser, model, request);
        var actualUser = request.getSession().getAttribute("user");
        var actualMessage = model.getAttribute("error");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualUser).isNull();
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    /**
     * Удаление данных о пользователе в сессии, перенаправление на страницу /users/login
     */
    @Test
    public void whenRequestLogoutThenGetLoginsPage() {
        String expectedPage = "redirect:/users/login";
        MockHttpSession session = new MockHttpSession();

        var actualPage = userController.logout(session);
        var actualInvalid = session.isInvalid();

        assertThat(actualInvalid).isEqualTo(true);
        assertThat(actualPage).isEqualTo(expectedPage);
    }
}
