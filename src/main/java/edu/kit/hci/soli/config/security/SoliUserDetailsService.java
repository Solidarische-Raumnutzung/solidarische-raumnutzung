package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.config.SoliConfiguration;
import edu.kit.hci.soli.service.SystemConfigurationService;
import edu.kit.hci.soli.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class SoliUserDetailsService implements UserDetailsService {
    // E-Mail pattern taken verbatim from OWASP Validation Regex Repository
    private static final Pattern mailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");

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
        } else if (systemConfigurationService.isGuestLoginEnabled()) {
            if (username.startsWith("guest/")) {
                return new SoliGuestUserDetails(
                        userService.resolveGuestUser(username),
                        "{noop}" + guestMarker
                );
            } else {
                if (mailPattern.matcher(username).matches()) {
                    return new SoliGuestUserDetails(
                            userService.createGuestUser(username),
                            "{noop}" + guestMarker
                    );
                } else {
                    throw new UsernameNotFoundException("Invalid E-Mail address: " + username);
                }
            }
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
