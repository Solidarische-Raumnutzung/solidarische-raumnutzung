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

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookingsService bookingsService;

    /**
     * Constructs a UserService with the specified {@link UserRepository} and {@link BookingsService}.
     *
     * @param userRepository the repository for managing User entities
     * @param bookingsService the service for managing bookings
     */
    public UserServiceImpl(UserRepository userRepository, BookingsService bookingsService) {
        this.userRepository = userRepository;
        this.bookingsService = bookingsService;
    }

    @Override
    public @Nullable User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public @NotNull User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public void toggleUserEnabled(User user) {
        if (!user.isDisabled()) {
            bookingsService.deleteAllBookingsForUser(user);
        }
        user.setDisabled(!user.isDisabled());
        userRepository.save(user);
    }

    @Override
    public @NotNull List<User> getManageableUsers() {
        return userRepository.findAllWithoutAdmin();
    }

    @Override
    public @Nullable User getById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

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

    @Override
    public @NotNull User resolveAdminUser() {
        User user = userRepository.findByUserId("admin");
        if (user == null) {
            log.error("No admin user found in database, creating new");
            user = userRepository.save(new User(null, "admin", null, "admin", false, Locale.getDefault()));
        }
        return user;
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getUserId().equals("admin");
    }

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

    @Override
    public boolean isGuestEnabled() {
        return true;
    }

    @Override
    public boolean isGuest(User user) {
        return user.getUserId().startsWith("guest/");
    }
}
