package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

/**
 * Service class for managing {@link User} entities.
 * Provides methods for user creation, retrieval, and management.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookingsService bookingsService;

    /**
     * Constructs a UserService with the specified {@link UserRepository} and {@link BookingsServiceImpl}.
     *
     * @param userRepository the repository for managing User entities
     * @param bookingsService the service for managing bookings
     */
    public UserServiceImpl(UserRepository userRepository, BookingsService bookingsService) {
        this.userRepository = userRepository;
        this.bookingsService = bookingsService;
    }

    /**
     * Finds a user by their (external) user ID.
     *
     * @param userId the user ID of the user
     * @return the user with the specified user ID, or null if not found
     */
    @Override
    public @Nullable User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * Creates a new user.
     *
     * @param user the user to be created
     * @return the created user
     */
    @Override
    public @NotNull User create(User user) {
        return userRepository.save(user);
    }

    /**
     * Disables or re-enables a user. If disabling, deletes all bookings for the user.
     *
     * @param user the user to be disabled or re-enabled
     */
    @Override
    public void toggleUserEnabled(User user) {
        if (!user.isDisabled()) {
            bookingsService.deleteAllBookingsForUser(user);
        }
        user.setDisabled(!user.isDisabled());
        userRepository.save(user);
    }

    /**
     * Retrieves all users that can be managed.
     *
     * @return a list of manageable users
     */
    @Override
    public @NotNull List<User> getManageableUsers() {
        return userRepository.findAllWithoutAdmin();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user
     * @return the user with the specified ID, or null if not found
     */
    @Override
    public @Nullable User getById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Resolves an {@link OidcUser} to a {@link User} entity. If the user does not exist, creates a new one.
     *
     * @param oidcUser the OIDC user to be resolved
     * @return the resolved user
     */
    @Override
    public @NotNull User resolveOidcUser(OidcUser oidcUser) {
        String userId = "kit/" + oidcUser.getName();
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            log.info("No OIDC user found in database for {}, creating new", userId);
            user = userRepository.save(new User(null, oidcUser.getPreferredUsername(), oidcUser.getEmail(), userId, false, Locale.getDefault()));
        }
        return user;
    }

    /**
     * Resolves the admin user. If the admin user does not exist, create it.
     *
     * @return the resolved admin user
     */
    @Override
    public @NotNull User resolveAdminUser() {
        User user = userRepository.findByUserId("admin");
        if (user == null) {
            log.error("No admin user found in database, creating new");
            user = userRepository.save(new User(null, "admin", null, "admin", false, Locale.getDefault()));
        }
        return user;
    }

    /**
     * Checks if a user is an admin.
     *
     * @param user the user to be checked
     * @return true if the user is an admin, false otherwise
     */
    @Override
    public boolean isAdmin(User user) {
        return user.getUserId().equals("admin");
    }

    /**
     * Resolves a guest user by their email. If the guest user does not exist, creates a new one.
     *
     * @param email the email of the guest user
     * @return the resolved guest user
     */
    @Override
    public @NotNull User resolveGuestUser(String email) {
        String id = "guest/" + email;
        User user = userRepository.findByUserId(id);
        if (user == null) {
            log.error("No guest user found in database, creating new");
            user = userRepository.save(new User(null, "Guest", email, id, false, Locale.getDefault()));
        }
        return user;
    }

    /**
     * Checks if guest users are enabled.
     *
     * @return true if guest users are enabled, false otherwise
     */
    @Override
    public boolean isGuestEnabled() {
        return true;
    }

    /**
     * Checks if a user is a guest.
     *
     * @param user the user to be checked
     * @return true if the user is a guest, false otherwise
     */
    @Override
    public boolean isGuest(User user) {
        return user.getUserId().startsWith("guest/");
    }
}
