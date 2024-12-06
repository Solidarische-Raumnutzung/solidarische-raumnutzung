package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class SoliAdminUserDetails implements SoliUserDetails {
    private final User user;
    private final String password;

    public SoliAdminUserDetails(User user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = SoliUserDetails.super.getAuthorities();
        result.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return result;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public User getUser() {
        return user;
    }
}
