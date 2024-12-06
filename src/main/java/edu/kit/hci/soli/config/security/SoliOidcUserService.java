package edu.kit.hci.soli.config.security;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SoliOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService oidcUserService = new OidcUserService();
    private final UserService userService;

    public SoliOidcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        String userId = "kit/" + oidcUser.getName();

        User user = userService.findByUserId(userId);
        if (user == null) {
            log.info("No OIDC user found in database for {}, creating new", userId);
            user = userService.create(new User(null, oidcUser.getPreferredUsername(), oidcUser.getEmail(), userId));
        }

        return new SoliOidcUserDetails(oidcUser, user);
    }

    public <T extends HttpSecurityBuilder<T>> OAuth2LoginConfigurer<T>.UserInfoEndpointConfig configure(OAuth2LoginConfigurer<T>.UserInfoEndpointConfig configurer) {
        return configurer.oidcUserService(this);
    }
}
