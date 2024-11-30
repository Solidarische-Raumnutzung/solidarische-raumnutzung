package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {

    private final UserRepository userRepository;

    public  UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByFullName(String fullname) {
        return userRepository.findByUsername(fullname);
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User resolveLoggedInUser(Principal principal) {
        return userRepository.findByUserId(principal.getName());
    }
}
