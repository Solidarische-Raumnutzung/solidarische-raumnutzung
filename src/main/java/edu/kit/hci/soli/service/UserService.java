package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

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
            user = userRepository.save(new User(null, "admin", null, "admin"));
        }
        return user;
    }

    public boolean isAdmin(User user) {
        return user.getUserId().equals("admin");
    }
}
