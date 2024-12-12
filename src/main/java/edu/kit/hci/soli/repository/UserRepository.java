package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link User} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds all users except the admin user.
     *
     * @return a list of users excluding the admin user
     */
    @Query("SELECT u FROM User u WHERE u.userId != 'admin'")
    List<User> findAllWithoutAdmin();

    /**
     * Finds a user by their (external) user ID.
     *
     * @param userId the user ID of the user
     * @return the user with the specified user ID
     */
    User findByUserId(String userId);
}
