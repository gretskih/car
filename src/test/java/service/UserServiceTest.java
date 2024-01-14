package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.UserRepository;
import ru.job4j.car.service.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Фабрика пользователей
     * @param login логин
     * @return User
     */
    public static User getUser(String login) {
        User user = new User();
        user.setName("Тест");
        user.setLogin(login);
        user.setPassword("0000");
        user.setContact("login@login.com");
        return user;
    }

    /**
     * Создание новой записи
     */
    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void whenCreateUserThenGetUser() {
        User expectedUser = getUser("user1");
        when(userRepository.create(userArgumentCaptor.capture())).thenReturn(Optional.of(expectedUser));

        var actualUser = userService.create(expectedUser);
        var actualUserCaptor = userArgumentCaptor.getValue();

        assertThat(actualUser.get()).usingRecursiveComparison().isEqualTo(expectedUser);
        assertThat(actualUserCaptor).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    /**
     * Поиск по login и password
     */
    @Captor
    ArgumentCaptor<String> loginArgumentCaptor;
    @Captor
    ArgumentCaptor<String> passwordArgumentCaptor;

    @Test
    public void whenFindUserByLoginAndPasswordThenGetUser() {
        String expectedLogin = "user2";
        String expectedPassword = "5dfds1";
        User expectedUser = getUser(expectedLogin);
        expectedUser.setPassword(expectedPassword);
        when(userRepository.findByLoginAndPassword(loginArgumentCaptor.capture(), passwordArgumentCaptor.capture()))
                .thenReturn(Optional.of(expectedUser));

        var actualUser = userService.findByLoginAndPassword(expectedLogin, expectedPassword);
        var actualLogin = loginArgumentCaptor.getValue();
        var actualPassword = passwordArgumentCaptor.getValue();

        assertThat(actualUser)
                .isPresent()
                .isNotEmpty()
                .contains(expectedUser);
        assertThat(actualLogin).isEqualTo(expectedLogin);
        assertThat(actualPassword).isEqualTo(expectedPassword);
    }
}
