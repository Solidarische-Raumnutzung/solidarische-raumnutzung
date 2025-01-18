package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.config.SoliConfiguration;
import edu.kit.hci.soli.service.SystemConfigurationService;
import edu.kit.hci.soli.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SoliUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final SystemConfigurationService systemConfigurationService;
    private final String adminPassword;
    private final String guestMarker;

    public SoliUserDetailsService(UserService userService, SystemConfigurationService systemConfigurationService, SoliConfiguration soliConfiguration) {
        this.userService = userService;
        this.systemConfigurationService = systemConfigurationService;
        this.adminPassword = soliConfiguration.getAdministrator().getPassword();
        this.guestMarker = soliConfiguration.getGuest().getMarker();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return new SoliAdminUserDetails(userService.resolveAdminUser(), adminPassword);
        }
        else if (systemConfigurationService.isGuestLoginEnabled()) {
            SoliGuestUserDetails userDetails = new SoliGuestUserDetails(
                    username.startsWith("guest/")
                            ? userService.resolveGuestUser(username)
                            : userService.createGuestUser(username),
                    "{noop}" + guestMarker
            );
            userService.updateLastLogin(userDetails.getUser());
            return userDetails;

        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
