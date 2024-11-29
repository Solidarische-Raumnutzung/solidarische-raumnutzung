package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.LoginStateModel;
import edu.kit.hci.soli.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import edu.kit.hci.soli.repository.VisitsRepository;
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
    private final VisitsRepository visitsRepository;

    private final UserService userService;

    public LoginControllerAdvice(VisitsRepository visitsRepository, UserService userService) {
        this.visitsRepository = visitsRepository;
        this.userService = userService;
    }

    @ModelAttribute("login")
    public LoginStateModel getLoginStateModel(Principal principal, @AuthenticationPrincipal OidcUser oidcUser, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        // TODO what if null?

        if (principal == null) {
            return new LoginStateModel("Visitor", visitsRepository.getVisits(), LoginStateModel.Kind.VISITOR, csrfToken);
        }
        if (oidcUser == null) {
            return new LoginStateModel(principal.getName(), visitsRepository.getVisits(), LoginStateModel.Kind.ADMIN, csrfToken);
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
        return new LoginStateModel(username, visitsRepository.getVisits(), LoginStateModel.Kind.OAUTH, csrfToken);
    }
}
