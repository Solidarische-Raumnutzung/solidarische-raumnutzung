package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public  UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @NotNull User create(User user) {
        return userRepository.save(user);
    }

    public @NotNull User resolveOidcUser(OidcUser oidcUser) {
        String userId = "kit/" + oidcUser.getName();
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            log.info("No OIDC user found in database for {}, creating new", userId);
            user = userRepository.save(new User(null, oidcUser.getPreferredUsername(), oidcUser.getEmail(), userId));
        }
        return user;
    }

    public @NotNull User resolveAdminUser() {
        User user = userRepository.findByUserId("admin");
        if (user == null) {
            log.error("No admin user found in database, creating new");
            user = userRepository.save(new User(null, "admin", null, "admin"));
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
            user = userRepository.save(new User(null, "Guest", email, id));
        }
        return user;
    }

    public boolean isGuestEnabled() {
        return true;
    }
}
