package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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

    public User resolveAdminUser() {
        return userRepository.findByUsername("admin");
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
