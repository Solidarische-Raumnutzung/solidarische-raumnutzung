package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Map;

public class SoliUserPrincipal implements OidcUser, SoliUserDetails {
    private final OidcUser oidcUser;
    private final User user;

    public SoliUserPrincipal(OidcUser oidcUser, User user) {
        this.oidcUser = oidcUser;
        this.user = user;
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return SoliUserDetails.super.getAuthorities();
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String getName() {
        return oidcUser.getName();
    }
}
