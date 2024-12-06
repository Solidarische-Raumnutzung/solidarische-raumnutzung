package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public interface SoliUserDetails extends UserDetails {
    @Override
    default List<SimpleGrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> result = new ArrayList<>(1);
        result.add(new SimpleGrantedAuthority("ROLE_USER"));
        return result;
    }

    @Override
    default String getPassword() {
        return "";
    }

    @Override
    default String getUsername() {
        return getUser().getUserId();
    }

    default String getDisplayName() {
        return getUser().getUsername();
    }

    User getUser();
}
