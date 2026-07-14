package hexlet.code.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hexlet.code.app.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
