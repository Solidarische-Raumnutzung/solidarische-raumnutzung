package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public void disableOrRenableUser(User user) {
        user.setDisabled(!user.isDisabled());
        userRepository.save(user);
    }

    public @NotNull List<User> getAll() {
        return userRepository.findAll();
    }

    public @Nullable User getById(Long userId) {
        return userRepository.findById(userId).orElse(null);
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
}
