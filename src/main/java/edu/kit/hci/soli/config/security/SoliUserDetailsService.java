package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class SoliUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Value("${soli.administrator.password}")
    private String adminPassword;

    @Value("${soli.guest.marker}")
    private String guestMarker;

    public SoliUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) return new SoliAdminUserDetails(userService.resolveAdminUser(), adminPassword);
        else if (userService.isGuestEnabled()) return new SoliGuestUserDetails(userService.resolveGuestUser(username), "{noop}" + guestMarker);
        else throw new UsernameNotFoundException("User not found: " + username);
    }
}
