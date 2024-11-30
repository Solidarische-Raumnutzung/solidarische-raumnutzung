package edu.kit.hci.soli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kit.hci.soli.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUsername(String username);

    User findByUserId(String userId);
}
