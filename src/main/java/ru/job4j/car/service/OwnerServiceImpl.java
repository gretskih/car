package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Owner;
import ru.job4j.car.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public Owner create(Owner owner) {
        return ownerRepository.create(owner);
    }

    @Override
    public boolean update(Owner owner) {
        return ownerRepository.update(owner);
    }

    @Override
    public boolean delete(int ownerId) {
        return ownerRepository.delete(ownerId);
    }

    @Override
    public List<Owner> findAllOrderById() {
        return ownerRepository.findAllOrderById();
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
    public Optional<Owner> findByUserId(int userId) {
        return ownerRepository.findByUserId(userId);
    }
}
