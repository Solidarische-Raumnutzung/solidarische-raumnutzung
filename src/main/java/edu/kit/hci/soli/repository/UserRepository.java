package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUsername(String username);

    User findByUserId(String userId);
}
