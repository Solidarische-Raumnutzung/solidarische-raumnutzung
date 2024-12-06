package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class SoliGuestUserDetails implements SoliUserDetails {
    private final User user;
    private final String password;

    public SoliGuestUserDetails(User user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = new ArrayList<>(1);
        result.add(new SimpleGrantedAuthority("ROLE_GUEST"));
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
