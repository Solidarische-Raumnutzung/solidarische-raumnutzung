package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userId != 'admin'")
    List<User> findAllWithoutAdmin();

    User findByEmail(String email);

    User findByUsername(String username);

    User findByUserId(String userId);
}
