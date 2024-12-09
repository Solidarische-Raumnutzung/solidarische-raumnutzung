package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final BookingsService bookingsService;

    public  UserService(UserRepository userRepository, BookingsService bookingsService) {
        this.userRepository = userRepository;
        this.bookingsService = bookingsService;
    }

    public @Nullable User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public @Nullable User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public @NotNull User create(User user) {
        return userRepository.save(user);
    }

    public void disableOrReenableUser(User user) {
        if(!user.isDisabled()) {
            bookingsService.deleteAllBookingsForUser(user);
        }

        user.setDisabled(!user.isDisabled());
        userRepository.save(user);
    }

    public @NotNull List<User> getManageableUsers() {
        return userRepository.findAllWithoutAdmin();
    }

    public @Nullable User getById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public @NotNull User resolveOidcUser(OidcUser oidcUser) {
        String userId = "kit/" + oidcUser.getName();
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            log.info("No OIDC user found in database for {}, creating new", userId);
            user = userRepository.save(new User(null, oidcUser.getPreferredUsername(), oidcUser.getEmail(), userId, false));
        }
        return user;
    }

    public @NotNull User resolveAdminUser() {
        User user = userRepository.findByUserId("admin");
        if (user == null) {
            log.error("No admin user found in database, creating new");
            user = userRepository.save(new User(null, "admin", null, "admin", false));
        }
        return user;
    }

    public boolean isAdmin(User user) {
        return user.getUserId().equals("admin");
    }

    public @NotNull User resolveGuestUser(String email) {
        String id = "guest/" + email;
        User user = userRepository.findByUserId(id);
        if (user == null) {
            log.error("No guest user found in database, creating new");
            user = userRepository.save(new User(null, "Guest", email, id, false));
        }
        return user;
    }

    public boolean isGuestEnabled() {
        return true;
    }

    public boolean isGuest(User user) {
        return user.getUserId().startsWith("guest/");
    }
}
