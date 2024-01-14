package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.dto.PostPreview;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.mappers.PostPreviewMapper;
import ru.job4j.car.mappers.PostViewMapper;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.PostRepository;
import ru.job4j.car.service.PostServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostPreviewMapper postPreviewMapper;
    @Mock
    private PostViewMapper postViewMapper;
    @InjectMocks
    private PostServiceImpl postService;
    private int idPost = 0;

    private Post getPost(String postDescription) {
        Post post = new Post();
        post.setId(++idPost);
        post.setCar(new Car());
        post.getCar().setId(idPost);
        post.setDescription(postDescription);
        post.setUser(new User());
        post.getUser().setId(idPost);
        return post;
    }

    @Captor
    ArgumentCaptor<Integer> integerArgumentCaptor;
    @Captor
    ArgumentCaptor<Post> postArgumentCaptor;
    @Captor
    ArgumentCaptor<Car> carArgumentCaptor;
    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    /**
     * Поиск объявления по id
     */
    @Test
    public void whenFindPostByIdThenGetPostView() {
        int expectedId = 1;
        Post exceptedPost = getPost("post1");
        PostView expectedPostView = new PostView();
        expectedPostView.setName("namePostView");
        when(postRepository.findById(integerArgumentCaptor.capture())).thenReturn(Optional.of(exceptedPost));
        when(postViewMapper.getPostView(postArgumentCaptor.capture(), carArgumentCaptor.capture(),
                userArgumentCaptor.capture())).thenReturn(expectedPostView);

        var actualOptionalPostView = postService.findById(expectedId);

        assertThat(actualOptionalPostView)
                .isPresent()
                .isNotEmpty()
                .contains(expectedPostView);
        assertThat(integerArgumentCaptor.getValue()).isEqualTo(expectedId);
        assertThat(postArgumentCaptor.getValue()).isEqualTo(exceptedPost);
        assertThat(carArgumentCaptor.getValue()).isEqualTo(exceptedPost.getCar());
        assertThat(userArgumentCaptor.getValue()).isEqualTo(exceptedPost.getUser());
    }

    /**
     * Неудачный поиск объявления по id
     */
    @Test
    public void whenFindPostByIdThenGetEmptyPostView() {
        int expectedId = 1;
        when(postRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        var actualOptionalPostView = postService.findById(expectedId);

        assertThat(actualOptionalPostView).isEmpty();
    }

    /**
     * Показать все объявления PostPreview
     */
    @Test
    public void whenFindAllPostPreviewThenGetAllPostPreviews() {
        Post exceptedPost1 = getPost("post2");
        Post exceptedPost2 = getPost("post3");

        PostPreview expectedPostPreview1 = new PostPreview();
        expectedPostPreview1.setName("namePostPreview2");
        PostPreview expectedPostPreview2 = new PostPreview();
        expectedPostPreview2.setName("namePostPreview3");

        when(postRepository.getPosts()).thenReturn(List.of(exceptedPost1, exceptedPost2));
        when(postPreviewMapper.getCarPreview(carArgumentCaptor.capture(), postArgumentCaptor.capture()))
                .thenReturn(expectedPostPreview1, expectedPostPreview2);

        var actualPostPreviews = postService.findAllPostPreview();

        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(List.of(expectedPostPreview1, expectedPostPreview2));
        assertThat(postArgumentCaptor.getAllValues()).isEqualTo(List.of(exceptedPost1, exceptedPost2));
        assertThat(carArgumentCaptor.getAllValues()).isEqualTo(List.of(exceptedPost1.getCar(), exceptedPost2.getCar()));
    }

    /**
     * Показать все объявления PostPreview
     * Объявления отсутствуют
     */
    @Test
    public void whenFindAllPostPreviewThenGetEmpty() {
        when(postRepository.getPosts()).thenReturn(Collections.emptyList());

        var actualPostPreviews = postService.findAllPostPreview();

        assertThat(actualPostPreviews).isEmpty();
    }

    /**
     * Показать все объявления PostPreview за последний день
     */
    @Test
    public void whenFindAllPostPreviewLastDayThenGetPostPreviews() {
        Post exceptedPost1 = getPost("post4");
        Post exceptedPost2 = getPost("post5");

        PostPreview expectedPostPreview1 = new PostPreview();
        expectedPostPreview1.setName("namePostPreview4");
        PostPreview expectedPostPreview2 = new PostPreview();
        expectedPostPreview2.setName("namePostPreview5");

        when(postRepository.getPostsLastDay()).thenReturn(List.of(exceptedPost1, exceptedPost2));
        when(postPreviewMapper.getCarPreview(carArgumentCaptor.capture(), postArgumentCaptor.capture()))
                .thenReturn(expectedPostPreview1, expectedPostPreview2);

        var actualPostPreviews = postService.getPostPreviewsLastDay();

        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(List.of(expectedPostPreview1, expectedPostPreview2));
        assertThat(postArgumentCaptor.getAllValues()).isEqualTo(List.of(exceptedPost1, exceptedPost2));
        assertThat(carArgumentCaptor.getAllValues()).isEqualTo(List.of(exceptedPost1.getCar(), exceptedPost2.getCar()));
    }

    /**
     * Показать все объявления PostPreview за последний день
     * Объявления отсутствуют
     */
    @Test
    public void whenFindAllPostPreviewLastDayThenGetEmpty() {
        when(postRepository.getPostsLastDay()).thenReturn(Collections.emptyList());

        var actualPostPreviews = postService.getPostPreviewsLastDay();

        assertThat(actualPostPreviews).isEmpty();
    }

    /**
     * Показать все объявления PostPreview с фото
     */
    @Test
    public void whenFindPostPreviewsWithPhotoThenGetPostPreviews() throws IOException {
        Files.createDirectories(Path.of("files"));
        PhotoDto photoDto = new PhotoDto("TestFile", new byte[] {1, 2, 3});
        String sourseName = "files" + java.io.File.separator + "TestFile";
        Files.write(Path.of(sourseName), photoDto.getContent());

        Photo expectedPhoto = new Photo(1, "TestFile", sourseName);

        Post exceptedPost1 = getPost("post6");
        Post exceptedPost2 = getPost("post7");

        exceptedPost1.getCar().setPhotos(Set.of(expectedPhoto));

        PostPreview expectedPostPreview1 = new PostPreview();
        expectedPostPreview1.setName("namePostPreview6");
        expectedPostPreview1.setId(1);
        PostPreview expectedPostPreview2 = new PostPreview();
        expectedPostPreview2.setName("namePostPreview7");
        expectedPostPreview2.setId(2);

        when(postRepository.getPosts()).thenReturn(List.of(exceptedPost1, exceptedPost2));
        when(postPreviewMapper.getCarPreview(carArgumentCaptor.capture(), postArgumentCaptor.capture()))
                .thenReturn(expectedPostPreview1, expectedPostPreview2);

        var actualPostPreviews = postService.getPostPreviewsWithPhoto();

        Files.deleteIfExists(Path.of(sourseName));

        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(List.of(expectedPostPreview1));
        assertThat(carArgumentCaptor.getAllValues()).usingRecursiveComparison().isEqualTo(List.of(exceptedPost1.getCar()));
        assertThat(postArgumentCaptor.getAllValues()).usingRecursiveComparison().isEqualTo(List.of(exceptedPost1));
    }

    /**
     * Показать все объявления PostPreview с фото
     * Объявления с фото отсутствуют
     */
    @Test
    public void whenFindPostPreviewsWithPhotoThenGetEmpty() throws IOException {
        Files.createDirectories(Path.of("files"));

        PhotoDto photoDto1 = new PhotoDto("TestFile1", new byte[]{});
        String sourseName1 = "files" + java.io.File.separator + "TestFile1";
        Files.write(Path.of(sourseName1), photoDto1.getContent());
        Photo expectedPhoto1 = new Photo(1, "TestFile1", sourseName1);

        PhotoDto photoDto2 = new PhotoDto("TestFile2", new byte[]{});
        String sourseName2 = "files" + java.io.File.separator + "TestFile2";
        Files.write(Path.of(sourseName2), photoDto2.getContent());
        Photo expectedPhoto2 = new Photo(2, "TestFile2", sourseName2);

        Post exceptedPost1 = getPost("post6");
        Post exceptedPost2 = getPost("post7");

        exceptedPost1.getCar().setPhotos(Set.of(expectedPhoto1));
        exceptedPost2.getCar().setPhotos(Set.of(expectedPhoto2));

        when(postRepository.getPosts()).thenReturn(List.of(exceptedPost1, exceptedPost2));

        var actualPostPreviews = postService.getPostPreviewsWithPhoto();

        Files.deleteIfExists(Path.of(sourseName1));
        Files.deleteIfExists(Path.of(sourseName2));

        assertThat(actualPostPreviews).isEmpty();
    }

    /**
     * Показать все объявления PostPreview пользователя user
     */
    @Test
    public void whenFindPostPreviewsUserThenGetPostPreviews() {
        Post exceptedPost1 = getPost("post8");
        Post exceptedPost2 = getPost("post9");

        PostPreview expectedPostPreview1 = new PostPreview();
        expectedPostPreview1.setName("namePostPreview8");
        PostPreview expectedPostPreview2 = new PostPreview();
        expectedPostPreview2.setName("namePostPreview9");
        User expectedUser = new User();
        expectedUser.setId(2);

        when(postRepository.getPostsUser(userArgumentCaptor.capture())).thenReturn(List.of(exceptedPost1,
                exceptedPost2));
        when(postPreviewMapper.getCarPreview(carArgumentCaptor.capture(), postArgumentCaptor.capture()))
                .thenReturn(expectedPostPreview1, expectedPostPreview2);

        var actualPostPreviews = postService.getPostPreviewsUser(expectedUser);

        assertThat(actualPostPreviews).usingRecursiveComparison().isEqualTo(List.of(expectedPostPreview1,
                expectedPostPreview2));
        assertThat(postArgumentCaptor.getAllValues()).isEqualTo(List.of(exceptedPost1, exceptedPost2));
        assertThat(carArgumentCaptor.getAllValues()).isEqualTo(List.of(exceptedPost1.getCar(), exceptedPost2.getCar()));
        assertThat(userArgumentCaptor.getValue()).isEqualTo(expectedUser);
    }

    /**
     * Показать все объявления PostPreview пользователя user
     * Объявления отсутствуют
     */
    @Test
    public void whenFindPostPreviewsUserThenGetEmpty() {
        User expectedUser = new User();
        expectedUser.setId(2);

        when(postRepository.getPostsUser(any(User.class))).thenReturn(Collections.emptyList());

        var actualPostPreviews = postService.getPostPreviewsUser(expectedUser);

        assertThat(actualPostPreviews).isEmpty();
    }
}
