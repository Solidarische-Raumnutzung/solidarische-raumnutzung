package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.NonOidcUser;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public LoginStateModel getLoginStateModel(Principal principal, @AuthenticationPrincipal OidcUser oidcUser, HttpServletRequest request, @ModelAttribute("csrf") CsrfToken csrf) {
        if (principal == null) {
            return new LoginStateModel("Visitor", LoginStateModel.Kind.VISITOR, csrf, null);
        }
        if (oidcUser == null) {
            User user = userService.resolveAdminUser();
            if (user == null) {
                log.error("No admin user found in database, creating new");
                user = new NonOidcUser();
                user.setEmail(null);
                user.setUsername("admin");
                user.setUserId("admin");
                user = userService.create(user);
            }
            return new LoginStateModel(principal.getName(), LoginStateModel.Kind.ADMIN, csrf, user);
        }

        String username = oidcUser.getUserInfo().getFullName();

        String id = "kit/" + principal.getName();
        User user = userService.findByUserId(id);
        if (user == null) {
            log.info("Creating new OIDC user: {} with id: {}", username, id);
            user = new edu.kit.hci.soli.domain.OidcUser();
            user.setEmail(oidcUser.getUserInfo().getEmail());
            user.setUsername(username);
            user.setUserId(id);
            user = userService.create(user);
        }
        return new LoginStateModel(username, LoginStateModel.Kind.OAUTH, csrf, user);
    }
}
