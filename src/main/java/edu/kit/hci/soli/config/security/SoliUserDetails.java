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

    class Admin implements SoliUserDetails {
        private final User user;
        private final String password;

        public Admin(User user, String password) {
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

    class Guest implements SoliUserDetails {
        private final User user;
        private final String password;

        public Guest(User user, String password) {
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
}
