package ru.job4j.car.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.user.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> create(User user) {
        return userRepository.create(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
