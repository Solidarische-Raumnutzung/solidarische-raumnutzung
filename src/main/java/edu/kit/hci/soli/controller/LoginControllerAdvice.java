package edu.kit.hci.soli.controller;

import jakarta.servlet.http.HttpServletRequest;
import edu.kit.hci.soli.domain.LoginStateModel;
import edu.kit.hci.soli.repository.VisitsRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class LoginControllerAdvice {
    private final VisitsRepository visitsRepository;

    public LoginControllerAdvice(VisitsRepository visitsRepository) {
        this.visitsRepository = visitsRepository;
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
        return new LoginStateModel(username, visitsRepository.getVisits(), LoginStateModel.Kind.OAUTH, csrfToken);
    }
}
