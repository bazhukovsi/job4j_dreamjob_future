package dreamjob.service;

import dreamjob.model.User;
import dreamjob.repository.Sql2oUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SimpleUserService implements UserService {

    private final Sql2oUserRepository userRepository;

    public SimpleUserService(Sql2oUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
