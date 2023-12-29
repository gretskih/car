package ru.job4j.car.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.car.dto.CarPreview;
import ru.job4j.car.model.Car;

@Mapper(componentModel = "spring")
public interface CarPreviewMapper {
    @Mapping(target = "engine", source = "engine.name")
    @Mapping(target = "owner", source = "owner.name")
    CarPreview getCarPreview(Car car);
}
