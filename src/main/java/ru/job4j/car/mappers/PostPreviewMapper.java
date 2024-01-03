package ru.job4j.car.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;

@Mapper(componentModel = "spring")
public interface PostPreviewMapper {
    @Mapping(target = "engine", source = "car.engine.name")
    @Mapping(target = "owner", source = "car.owner.name")
    @Mapping(target = "price", source = "post.price")
    PostPreview getCarPreview(Car car, Post post);
}
