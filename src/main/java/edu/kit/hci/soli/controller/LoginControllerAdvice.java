package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.LoginStateModel;
import edu.kit.hci.soli.repository.VisitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class LoginControllerAdvice {
    @Autowired
    private VisitsRepository visitsRepository;

    @ModelAttribute("login")
    public LoginStateModel getLoginStateModel(Principal principal, @AuthenticationPrincipal OidcUser oidcUser) {
        if (principal == null) {
            return new LoginStateModel("Anonymous", visitsRepository.getVisits());
        }
        if (oidcUser == null) {
            return new LoginStateModel("", visitsRepository.getVisits());
        }
        String username = oidcUser.getUserInfo().getFullName();
        return new LoginStateModel(username, visitsRepository.getVisits());
    }
}
