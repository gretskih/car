package service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.OwnerRepositoryImpl;
import ru.job4j.car.service.OwnerServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {
    @Mock
    private OwnerRepositoryImpl ownerRepository;
    @InjectMocks
    private OwnerServiceImpl ownerService;

    /**
     * Фабрика пользователей
     * @param login логин
     * @return User пользователь
     */
    public static User getUser(String login) {
        User user = new User();
        user.setId(1);
        user.setName("Тест");
        user.setLogin(login);
        user.setPassword("0000");
        user.setContact("login@login.com");
        return user;
    }

    /**
     * Фабрика владельцев
     * @param name имя
     * @param user пользователь
     * @return владелец
     */
    public static Owner getOwner(String name, User user) {
        return Owner.of()
                .id(1)
                .name(name)
                .user(user)
                .build();
    }

    /**
     * Создание новой записи
     */
    @Test
    public void whenCreateOwnerThenGetOwner() {
        Owner expectedOwner = getOwner("Name", getUser("Login"));
        var ownerCaptor = ArgumentCaptor.forClass(Owner.class);
        when(ownerRepository.create(ownerCaptor.capture())).thenReturn(expectedOwner);

        var actualOwner = ownerService.create(expectedOwner);
        var actualOwnerCaptor = ownerCaptor.getValue();

        assertThat(actualOwner).usingRecursiveComparison().isEqualTo(expectedOwner);
        assertThat(actualOwnerCaptor).usingRecursiveComparison().isEqualTo(expectedOwner);
    }

    /**
     * Обновление записи
     */
    @Test
    public void whenUpdateOwnerThenGetTrue() {
        Owner expectedOwner = getOwner("Name", getUser("Login"));
        var ownerCaptor = ArgumentCaptor.forClass(Owner.class);
        when(ownerRepository.update(ownerCaptor.capture())).thenReturn(true);

        var resultTransaction = ownerService.update(expectedOwner);
        var actualOwnerCaptor = ownerCaptor.getValue();

        Assertions.assertThat(resultTransaction).isTrue();
        Assertions.assertThat(actualOwnerCaptor).isEqualTo(expectedOwner);
    }

    /**
     * Удаление записи
     */
    @Test
    public void whenDeleteOwnerByIdThenGetTrue() {
        Owner expectedOwner = getOwner("Name", getUser("Login"));
        var captureOwner = ArgumentCaptor.forClass(Owner.class);
        when(ownerRepository.delete(captureOwner.capture())).thenReturn(true);

        var resultTransaction = ownerService.delete(expectedOwner);
        var actualOwner = captureOwner.getValue();

        Assertions.assertThat(resultTransaction).isTrue();
        Assertions.assertThat(actualOwner).isEqualTo(expectedOwner);
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOwnerOrderByIdThenGetAllOwners() {
        Owner expectedOwner1 = getOwner("Name1", getUser("Login1"));
        Owner expectedOwner2 = getOwner("Name2", getUser("Login2"));
        List<Owner> expectedOwners = List.of(expectedOwner1, expectedOwner2);
        when(ownerRepository.findAll()).thenReturn(expectedOwners);

        var actualOwners = ownerService.findAll();

        assertThat(actualOwners).isEqualTo(expectedOwners);
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindOwnerByIdThenGetOwner() {
        Owner expectedOwner = getOwner("Name", getUser("Login"));
        int ownerId = expectedOwner.getId();
        var captureOwnerId = ArgumentCaptor.forClass(int.class);
        when(ownerRepository.findById(captureOwnerId.capture())).thenReturn(Optional.of(expectedOwner));

        Optional<Owner> actualOwner = ownerService.findById(ownerId);

        assertThat(captureOwnerId.getValue()).isEqualTo(ownerId);
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }

    /**
     * Поиск по части имени
     */
    @Test
    public void whenFindOwnerByLikeNameThenGetOwner() {
        Owner expectedOwner = getOwner("Name", getUser("Login"));
        String expectedKey = "ogi";
        var captureKey = ArgumentCaptor.forClass(String.class);
        when(ownerRepository.findByLikeName(captureKey.capture())).thenReturn(List.of(expectedOwner));

        List<Owner> actualOwners = ownerService.findByLikeName(expectedKey);

        assertThat(captureKey.getValue()).isEqualTo(expectedKey);
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner));
    }

    /**
     * Поиск по имени name
     */
    @Test
    public void whenFindOwnerByNameThenGetOwner() {
        Owner expectedOwner = getOwner("Name", getUser("Login"));
        String expectedName = expectedOwner.getName();
        var captureName = ArgumentCaptor.forClass(String.class);
        when(ownerRepository.findByName(captureName.capture())).thenReturn(Optional.of(expectedOwner));

        Optional<Owner> actualOwner = ownerService.findByName(expectedName);

        assertThat(captureName.getValue()).isEqualTo(expectedName);
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }

    /**
     * Найти владельца по User
     */
    @Test
    public void whenFindOwnerByUserThenGetOwner() {
        Owner expectedOwner = getOwner("Name", getUser("Login"));
        User expectedUser = expectedOwner.getUser();
        var captureUser = ArgumentCaptor.forClass(User.class);
        when(ownerRepository.findByUser(captureUser.capture())).thenReturn(Optional.of(expectedOwner));

        Optional<Owner> actualOwner = ownerService.findByUser(expectedUser);

        assertThat(captureUser.getValue()).isEqualTo(expectedUser);
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }
}
