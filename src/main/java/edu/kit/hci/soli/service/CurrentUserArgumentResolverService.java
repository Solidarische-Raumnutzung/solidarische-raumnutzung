package edu.kit.hci.soli.service;

import com.sun.security.auth.UserPrincipal;
import edu.kit.hci.soli.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

@Slf4j
public class CurrentUserArgumentResolverService implements HandlerMethodArgumentResolver {

    private final UserService userService;

    public CurrentUserArgumentResolverService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Principal principal = webRequest.getUserPrincipal();

        if (principal instanceof OAuth2AuthenticationToken token) {
            User user = userService.resolveLoggedInUser(principal);

            if (user == null) {
                user = new User();
                user.setEmail((String) token.getPrincipal().getAttributes().get("email"));
                user.setUsername((String) token.getPrincipal().getAttributes().get("name"));
                user.setUserId("kit/" + principal.getName());

                log.info("Creating new OIDC user {}", user);

                user = userService.create(user);
            }

            return user;

        } else if (principal instanceof UsernamePasswordAuthenticationToken) {
            return userService.resolveAdminUser();
        }

        return null;
    }
}
