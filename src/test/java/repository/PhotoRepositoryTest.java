package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Photo;
import ru.job4j.car.repository.PhotoRepository;
import ru.job4j.car.repository.PhotoRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class PhotoRepositoryTest {
    public static PhotoRepository photoRepository = new PhotoRepositoryImpl(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTablesBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Создание / Удаление
     */
    @Test
    public void whenDeletePhotoThenGetEmptyPhoto() {
        Photo expectedPhoto = new Photo();
        expectedPhoto.setName("photo1");
        expectedPhoto.setPath("photoPath");
        photoRepository.save(expectedPhoto);

        boolean actualStatusTransaction = photoRepository.deleteById(expectedPhoto.getId());
        var actualPhoto = photoRepository.findById(expectedPhoto.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualPhoto).isEmpty();
    }

    /**
     * Поиск по идентификатору
     */
    @Test
    public void whenFindPhotoByIdThenGetPhoto() {
        Photo expectedPhoto = new Photo();
        expectedPhoto.setName("photo2");
        expectedPhoto.setPath("photoPath");
        photoRepository.save(expectedPhoto);

        var actualPhoto = photoRepository.findById(expectedPhoto.getId());

        assertThat(actualPhoto)
                .isPresent()
                .isNotEmpty()
                .contains(expectedPhoto);
    }
}
