package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
     * Disables or re-enables a user. If disabling, deletes all bookings for the user.
     *
     * @param user the user to be disabled or re-enabled
     */
    void toggleUserEnabled(User user);

    /**
     * Retrieves all users that can be managed.
     *
     * @return a list of manageable users
     */
    @NotNull List<User> getManageableUsers();

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
     * Resolves a guest user by their email. If the guest user does not exist, creates a new one.
     *
     * @param email the email of the guest user
     * @return the resolved guest user
     */
    @NotNull User resolveGuestUser(String email);

    /**
     * Checks if a user is a guest.
     *
     * @param user the user to be checked
     * @return true if the user is a guest, false otherwise
     */
    boolean isGuest(User user);

    /**
     * Deletes a user.
     * If the user has any bookings, this will not delete the user.
     *
     * @param user the user to be deleted
     * @return true if the user was deleted, false otherwise
     */
    boolean deleteUser(User user);
}
