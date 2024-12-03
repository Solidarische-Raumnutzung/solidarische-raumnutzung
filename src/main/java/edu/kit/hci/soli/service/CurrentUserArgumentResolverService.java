package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

public class CurrentUserArgumentResolverService implements HandlerMethodArgumentResolver {

    private final UserService userService;

    public CurrentUserArgumentResolverService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null
                && parameter.getParameterType().equals(User.class);    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Principal principal = (Principal) webRequest.getUserPrincipal();
        if (principal != null) {
            return userService.resolveLoggedInUser(principal);
        }
        return null;
    }
}
