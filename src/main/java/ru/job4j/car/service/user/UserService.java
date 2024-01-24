package ru.job4j.car.service.user;

import ru.job4j.car.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> create(User user);

    Optional<User> findByLoginAndPassword(String login, String password);
}
