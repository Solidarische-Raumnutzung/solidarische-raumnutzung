package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

/**
 * Service class for managing {@link User} entities.
 * Provides methods for user creation, retrieval, and management.
 */
public interface UserService {
    /**
     * Finds a user by their (external) user ID.
     *
     * @param userId the user ID of the user
     * @return the user with the specified user ID, or null if not found
     */
    @Nullable User findByUserId(String userId);

    /**
     * Deactivates or Reactivates a user.
     * If deactivating, deletes all bookings for the user.
     *
     * @param user the user to be reactivated
     * @param active the new active state of the user
     */
    void setUserActive(User user, boolean active);

    /**
     * Retrieves all users that can be managed.
     * @param page the page number
     *             (0-based, i.e., the first page is page 0)
     * @param size the number of users per page
     * @return a list of manageable users
     */
    @NotNull Page<User> getManageableUsers(int page, int size);

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user
     * @return the user with the specified ID, or null if not found
     */
    @Nullable User getById(Long userId);

    /**
     * Resolves an {@link OidcUser} to a {@link User} entity. If the user does not exist, creates a new one.
     *
     * @param oidcUser the OIDC user to be resolved
     * @return the resolved user
     */
    @NotNull User resolveOidcUser(OidcUser oidcUser);

    /**
     * Resolves the admin user. If the admin user does not exist, create it.
     *
     * @return the resolved admin user
     */
    @NotNull User resolveAdminUser();

    /**
     * Checks if a user is an admin.
     *
     * @param user the user to be checked
     * @return true if the user is an admin, false otherwise
     */
    boolean isAdmin(User user);

    /**
     * Resolves a guest user by their userId.
     * If the guest user does not exist, creates a new one.
     *
     * @param userId the email of the guest user
     * @return the resolved guest user
     */
    @NotNull User resolveGuestUser(String userId);

    /**
     * Creates a guest user with the given email.
     * The user ID will be generated.
     *
     * @param email the email of the guest user
     * @return the created guest user
     */
    @NotNull User createGuestUser(String email);

    /**
     * Checks if a user is a guest.
     *
     * @param user the user to be checked
     * @return true if the user is a guest, false otherwise
     */
    boolean isGuest(User user);

    /**
     * Resolves the anonymous user.
     *
     * This user is used for bookings that have been anonymized.
     *
     * @return the resolved anonymous user
     */
    @NotNull User resolveAnonUser();

    /**
     * Updates the user's last login timestamp.
     * This method is called whenever a user logs in.
     *
     * @param user the user whose last login timestamp is to be updated
     */
    void updateLastLogin(User user);


    /**
     * Deletes a user.
     * If the user has any bookings, this will not delete the user.
     *
     * @param user the user to be deleted
     * @return true if the user was deleted, false otherwise
     */
    boolean deleteUser(User user);
}
