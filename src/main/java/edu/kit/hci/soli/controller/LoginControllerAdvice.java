package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public LoginStateModel getLoginStateModel(Principal principal, @AuthenticationPrincipal User user, HttpServletRequest request, @ModelAttribute("csrf") CsrfToken csrf) {

        if (principal == null) {
            return new LoginStateModel("Visitor", LoginStateModel.Kind.VISITOR, csrf, null);
        } else if (userService.isAdmin(user)) {
            return new LoginStateModel(user.getUsername(), LoginStateModel.Kind.ADMIN, csrf, user);
        }

        return new LoginStateModel(user.getUsername(), LoginStateModel.Kind.OAUTH, csrf, user);
    }
}
