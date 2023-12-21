package mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.job4j.car.dto.PostDto;
import ru.job4j.car.dto.UserDto;
import ru.job4j.car.mappers.PostMapper;
import ru.job4j.car.mappers.UserMapper;
import ru.job4j.car.model.Post;
import ru.job4j.car.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MapperTest {

    @Test
    public void mapperFromPostAndFromPostDto() {
        PostMapper postMapper = Mappers.getMapper(PostMapper.class);
        Post postModel = new Post(1, "descr", LocalDateTime.now(), 2, new ArrayList<>());
        PostDto postDto = new PostDto(3, "descrdto", LocalDateTime.now(), 5);

        Post modelFromPostDto = postMapper.getModelFromDto(postDto);
        PostDto dtoFromPostModel = postMapper.getDtoFromModel(postModel);

        assertThat(postDto.getId()).isEqualTo(modelFromPostDto.getId());
        assertThat(postDto.getCreatedDate()).isEqualTo(modelFromPostDto.getCreated());
        assertThat(postDto.getDescription()).isEqualTo(modelFromPostDto.getDescription());
        assertThat(postDto.getUserId()).isEqualTo(modelFromPostDto.getUserId());

        assertThat(postModel.getId()).isEqualTo(dtoFromPostModel.getId());
        assertThat(postModel.getCreated()).isEqualTo(dtoFromPostModel.getCreatedDate());
        assertThat(postModel.getDescription()).isEqualTo(dtoFromPostModel.getDescription());
        assertThat(postModel.getUserId()).isEqualTo(dtoFromPostModel.getUserId());
    }

    @Test
    public void mapperFromUserAndFromUserDto() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        User userModel = new User(1, "user", "pass");
        UserDto userDto = new UserDto(2, "userdto", "passdto");

        User modelFromUserDto = userMapper.getModelFromDto(userDto);
        UserDto dtoFromUserModel = userMapper.getDtoFromModel(userModel);

        assertThat(userDto.getId()).isEqualTo(modelFromUserDto.getId());
        assertThat(userDto.getLoginDto()).isEqualTo(modelFromUserDto.getLogin());
        assertThat(userDto.getPassword()).isEqualTo(modelFromUserDto.getPassword());

        assertThat(userModel.getId()).isEqualTo(dtoFromUserModel.getId());
        assertThat(userModel.getLogin()).isEqualTo(dtoFromUserModel.getLoginDto());
        assertThat(userModel.getPassword()).isEqualTo(dtoFromUserModel.getPassword());
    }
}
