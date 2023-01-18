package dreamjob.repository;

import dreamjob.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);
}