package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controller advice for injecting the login state.
 */
@ControllerAdvice
@Slf4j
public class LayoutParamsAdvice {
    private final UserService userService;

    /**
     * Constructs a LoginControllerAdvice with the specified UserService.
     *
     * @param userService the service for managing users
     */
    public LayoutParamsAdvice(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the CSRF token from the request.
     *
     * @param request the HTTP request
     * @return the CSRF token
     */
    @ModelAttribute("csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    /**
     * Retrieves the login state model based on the authenticated user details.
     *
     * @param principal the authenticated user details
     * @param csrf      the CSRF token
     * @return the login state model
     */
    @ModelAttribute("login")
    public LoginStateModel getLoginStateModel(@AuthenticationPrincipal SoliUserDetails principal, @ModelAttribute("csrf") CsrfToken csrf) {
        if (principal == null) {
            return new LoginStateModel("Visitor", LoginStateModel.Kind.VISITOR, csrf, null);
        } else if (userService.isAdmin(principal.getUser())) {
            return new LoginStateModel(principal.getDisplayName(), LoginStateModel.Kind.ADMIN, csrf, principal.getUser());
        } else if (userService.isGuest(principal.getUser())) {
            return new LoginStateModel(principal.getDisplayName(), LoginStateModel.Kind.GUEST, csrf, principal.getUser());
        } else {
            return new LoginStateModel(principal.getDisplayName(), LoginStateModel.Kind.OAUTH, csrf, principal.getUser());
        }
    }

    /**
     * Retrieves the state of the site layout
     *
     * @param login the state of the users login
     * @param request the HTTP request
     * @return state of the site layout
     */
    @ModelAttribute("layout")
    public LayoutParams getLayoutParams(@ModelAttribute("login") LoginStateModel login, HttpServletRequest request) {
        return new LayoutParams(
                login,
                (Room) request.getSession().getAttribute("room"),
                room -> request.getSession().setAttribute("room", room));
    }
}
