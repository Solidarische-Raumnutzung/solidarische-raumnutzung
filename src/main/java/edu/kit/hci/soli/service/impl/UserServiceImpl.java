package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookingsService bookingsService;

    /**
     * Constructs a UserService with the specified {@link UserRepository} and {@link BookingsService}.
     *
     * @param userRepository  the repository for managing User entities
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
    public void setUserActive(User user, boolean active) {
        if (isAdmin(user) && !active) {
            throw new IllegalArgumentException("Cannot disable admin user");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User does not exist");
        }
        if (user.isDisabled() != active) {
            throw new IllegalArgumentException("User is not disabled");
        }
        if (!active) {
            bookingsService.deleteAllBookingsForUser(user);
        }
        user.setDisabled(!active);
        userRepository.save(user);
    }

    @Override
    public @NotNull Page<User> getManageableUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllWithoutAdmin(pageable);
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
    public @NotNull User resolveGuestUser(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            log.error("No guest user found in database, creating new");
            String email = userId.substring("guest/".length());
            email = email.substring(email.indexOf('/') + 1);
            user = userRepository.save(new User(null, "Guest", email, userId, false, Locale.getDefault()));
        }
        return user;
    }

    @Override
    public @NotNull User createGuestUser(String email) {
        String id;
        do {
            id = "guest/" + UUID.randomUUID() + "/" + email;
        } while (userRepository.existsByUserId(id));
        log.error("Creating new guest user with ID {}", id);
        return userRepository.save(new User(null, "Guest", email, id, false, Locale.getDefault()));
    }

    @Override
    public boolean isGuest(User user) {
        return user.getUserId().startsWith("guest/");
    }

    @Override
    public boolean deleteUser(User user) {
        if (isAdmin(user)) {
            throw new IllegalArgumentException("Cannot delete admin user");
        }
        if (!userRepository.existsById(user.getId()) || bookingsService.hasBookings(user)) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }
}
