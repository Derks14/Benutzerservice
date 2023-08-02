package tech11.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech11.models.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
