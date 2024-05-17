package za.ac.tut.maizemeal_shop.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import za.ac.tut.maizemeal_shop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailOrUsername(String email, String username);

    User findByEmailAndPassword(String emailOrUsername, String password);
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = ?1 OR u.username = ?2")
    Optional<User> findByEmailOrUsername(String email, String username);
}