package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public  UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nullable User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public @Nullable User findByFullName(String fullname) {
        return userRepository.findByUsername(fullname);
    }

    public @Nullable User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public @NotNull User create(User user) {
        return userRepository.save(user);
    }

    public @NotNull User resolveAdminUser() {
        User user = userRepository.findByUserId("admin");
        if (user == null) {
            log.error("No admin user found in database, creating new");
            user = new User();
            user.setEmail(null);
            user.setUsername("admin");
            user.setUserId("admin");
            user = userRepository.save(user);
        }
        return user;
    }

    public User resolveLoggedInUser(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            return userRepository.findByUserId("kit/" + principal.getName());
        } else if (principal instanceof UsernamePasswordAuthenticationToken) {
            return userRepository.findByUsername(principal.getName());
        } else {
            throw new IllegalArgumentException("Unknown principal type");
        }
    }
}
