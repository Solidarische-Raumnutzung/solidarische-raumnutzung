package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.LoginStateModel;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.VisitsRepository;
import edu.kit.hci.soli.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.logging.Logger;

@ControllerAdvice
public class LoginControllerAdvice {

    private final Logger logger = Logger.getLogger(LoginControllerAdvice.class.getName());


    private final VisitsRepository visitsRepository;

    private final UserService userService;

    public LoginControllerAdvice(VisitsRepository visitsRepository, UserService userService) {
        this.visitsRepository = visitsRepository;
        this.userService = userService;
    }

    @ModelAttribute("login")
    public LoginStateModel getLoginStateModel(Principal principal, @AuthenticationPrincipal OidcUser oidcUser) {
        if (principal == null) {
            return new LoginStateModel("Anonymous", visitsRepository.getVisits());
        }
        if (oidcUser == null) {
            return new LoginStateModel(principal.getName(), visitsRepository.getVisits());
        }


        String username = oidcUser.getUserInfo().getFullName();
        String email = oidcUser.getUserInfo().getEmail();
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.info("Creating new OIDC user: " + username + " with email: " + email);
            user = new User();
            user.setEmail(email);
            user.setUsername(username);
            userService.create(user);
        }

        return new LoginStateModel(username, visitsRepository.getVisits());
    }
}
