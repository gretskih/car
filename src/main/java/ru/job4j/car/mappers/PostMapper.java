package ru.job4j.car.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.car.dto.PostDto;
import ru.job4j.car.model.Post;

@Mapper
public interface PostMapper {
    @Mapping(target = "createdDate", source = "created")
    PostDto getDtoFromModel(Post post);

    @InheritInverseConfiguration
    Post getModelFromDto(PostDto postDto);
}
