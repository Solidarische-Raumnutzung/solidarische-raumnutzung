package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

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
    @Query("SELECT u FROM User u WHERE u.userId != 'admin' AND u.userId != 'anon'")
    Page<User> findAllWithoutAdminOrAnon(Pageable pageable);

    /**
     * Finds a user by their (external) user ID.
     *
     * @param userId the user ID of the user
     * @return the user with the specified user ID
     */
    User findByUserId(String userId);

    /**
     * Checks if a user with the given userId exists.
     *
     * @param userId the user ID to check for
     * @return true if a user with the given userId exists, false otherwise
     */
    boolean existsByUserId(@NotNull String userId);

    /**
     * Updates the user's last login timestamp.
     * This query is called whenever a user logs in.
     *
     * @param userId the user ID of the user
     */
    @Query("UPDATE User u SET u.lastLogin = CURRENT_TIMESTAMP WHERE u.userId = :userId")
    @Modifying
    void updateLastLogin(String userId);

    /**
     * Deletes all users that have not logged in since the specified date and have no bookings.
     *
     * @param date the date to compare the last login timestamp to
     */
    @Query("DELETE FROM User u WHERE u.userId != 'admin' AND u.userId != 'anon' AND u.lastLogin < :date AND NOT EXISTS (SELECT b FROM Booking b WHERE b.user = u)")
    @Modifying
    void deleteUnusedOlderThan(LocalDateTime date);
}
