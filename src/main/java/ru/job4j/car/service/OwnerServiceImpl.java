package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public Owner create(Owner owner) {
        try {
            return ownerRepository.create(owner);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean update(Owner owner) {
        return ownerRepository.update(owner);
    }

    @Override
    public boolean delete(Owner owner) {
        return ownerRepository.delete(owner);
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    public Optional<Owner> findById(int ownerId) {
        return ownerRepository.findById(ownerId);
    }

    @Override
    public List<Owner> findByLikeName(String key) {
        return ownerRepository.findByLikeName(key);
    }

    @Override
    public Optional<Owner> findByName(String name) {
        return ownerRepository.findByName(name);
    }

    @Override
    public Optional<Owner> findByUser(User user) {
        return ownerRepository.findByUserId(user.getId());
    }
}
