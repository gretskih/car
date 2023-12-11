package ru.job4j.car.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.car.dto.UserDto;
import ru.job4j.car.model.User;

@Mapper
public interface UserMapper {
    @Mapping(target = "loginDto", source = "login")
    UserDto getDtoFromModel(User user);

    @InheritInverseConfiguration
    User getModelFromDto(UserDto userDto);
}
