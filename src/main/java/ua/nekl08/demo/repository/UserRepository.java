package ua.nekl08.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nekl08.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
