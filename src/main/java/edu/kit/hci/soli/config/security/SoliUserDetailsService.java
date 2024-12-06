package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class SoliUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Value("${soli.administrator.password}")
    private String password;

    public SoliUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) return new SoliAdminUserDetails(userService.resolveAdminUser(), password);
        throw new UsernameNotFoundException("User not found");
    }
}
