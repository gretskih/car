package repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.CrudRepository;

public class ConfigurationTest {
    public static StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    public static SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    public static CrudRepository crudRepository = new CrudRepository(sf);
}
