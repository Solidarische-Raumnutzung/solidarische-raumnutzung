package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    public LoginStateModel getLoginStateModel(@AuthenticationPrincipal SoliUserDetails user, @ModelAttribute("csrf") CsrfToken csrf) {
        if (user == null) {
            return new LoginStateModel("Visitor", LoginStateModel.Kind.VISITOR, csrf, null);
        } else if (userService.isAdmin(user.getUser())) {
            return new LoginStateModel(user.getDisplayName(), LoginStateModel.Kind.ADMIN, csrf, user.getUser());
        } else {
            return new LoginStateModel(user.getDisplayName(), LoginStateModel.Kind.OAUTH, csrf, user.getUser());
        }
    }
}
