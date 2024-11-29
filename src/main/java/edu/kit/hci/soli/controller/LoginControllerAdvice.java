package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import edu.kit.hci.soli.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@Slf4j
public class LoginControllerAdvice {
    private final UserService userService;

    public LoginControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @ModelAttribute("login")
    public LoginStateModel getLoginStateModel(Principal principal, @AuthenticationPrincipal OidcUser oidcUser, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        // TODO what if null?

        //TODO the user should NOT be null, since we need it in templates
        if (principal == null) {
            return new LoginStateModel("Visitor", LoginStateModel.Kind.VISITOR, csrfToken, null);
        }
        if (oidcUser == null) {
            return new LoginStateModel(principal.getName(), LoginStateModel.Kind.ADMIN, csrfToken, null);
        }


        String username = oidcUser.getUserInfo().getFullName();

        String email = oidcUser.getUserInfo().getEmail();
        User user = userService.findByEmail(email);
        if (user == null) {
            log.info("Creating new OIDC user: {} with email: {}", username, email);
            user = new User();
            user.setEmail(email);
            user.setUsername(username);
            userService.create(user);
        }
        return new LoginStateModel(username, LoginStateModel.Kind.OAUTH, csrfToken, user);
    }
}
