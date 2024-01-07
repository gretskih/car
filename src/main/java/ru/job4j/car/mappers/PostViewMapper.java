package ru.job4j.car.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;

@Mapper(componentModel = "spring")
public interface PostViewMapper {
    @Mapping(target = "engine", source = "car.engine.name")
    @Mapping(target = "owner", source = "car.owner.name")
    @Mapping(target = "price", source = "post.price")
    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "car.name")
    PostView getPostView(Post post, Car car, User user);
}
