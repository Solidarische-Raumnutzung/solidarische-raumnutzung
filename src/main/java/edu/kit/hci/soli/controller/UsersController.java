package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.SystemConfigurationService;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing user-related operations.
 */
@Controller
@Slf4j
public class UsersController {
    private final UserService userService;
    private final SystemConfigurationService systemConfigurationService;

    /**
     * Constructs a UsersController with the specified {@link UserService}.
     *
     * @param userService the service for managing users
     */
    public UsersController(UserService userService, SystemConfigurationService systemConfigurationService) {
        this.userService = userService;
        this.systemConfigurationService = systemConfigurationService;
    }

    /**
     * Deactivates a user by their ID.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param userId    the ID of the user to be deactivated
     * @return the view name
     */
    @PutMapping("/admin/users/{userId}/deactivate")
    public String deactivateUser(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal, @PathVariable Long userId) {
        log.info("User {} requested to deactivate user {}", principal.getUser(), userId);
        User user = userService.getById(userId);

        if (user == null) {
            log.info("User {} not found", userId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        if (user.isDisabled()) {
            log.info("User {} is already deactivated", user);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        if ("admin".equals(user.getUsername())) {
            return "redirect:/admin/users";
        }

        userService.setUserActive(user, false);
        log.info("User {} deactivated user {}", principal.getUser(), user);

        return "redirect:/admin/users";
    }

    /**
     * Reactivates a user by their ID.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param userId    the ID of the user to be deactivated
     * @return the view name
     */
    @PutMapping("/admin/users/{userId}/reactivate")
    public String reactivateUser(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal, @PathVariable Long userId) {
        log.info("User {} requested to deactivate user {}", principal.getUser(), userId);
        User user = userService.getById(userId);

        if (user == null) {
            log.info("User {} not found", userId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        if (!user.isDisabled()) {
            log.info("User {} is already active", user);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        if ("admin".equals(user.getUsername())) {
            return "redirect:/admin/users";
        }

        userService.setUserActive(user, true);
        log.info("User {} reactivated user {}", principal.getUser(), user);

        return "redirect:/admin/users";
    }

    /**
     * Retrieves all manageable users and returns the view for the users page.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @return the view name
     */
    @GetMapping("/admin/users")
    public String getUsers(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} requested the users page", principal.getUser());
        model.addAttribute("users", userService.getManageableUsers());
        model.addAttribute("guestsEnabled", systemConfigurationService.isGuestLoginEnabled());
        return "users";
    }

    @GetMapping("/admin/users/disable-guests")
    public String disableGuestsConfirmation(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} opened the dialog to disable guest login", principal.getUser());
        if (!systemConfigurationService.isGuestLoginEnabled()) {
            log.warn("Guest login is already disabled");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        return "disable_guests_confirmation";
    }

    @PutMapping("/admin/users/disable-guests")
    public String disableGuests(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} requested to disable guests", principal.getUser());
        if (!systemConfigurationService.isGuestLoginEnabled()) {
            log.warn("Guest login is already disabled");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        systemConfigurationService.setGuestLoginEnabled(false);
        return "redirect:/admin/users";
    }

    @PutMapping("/admin/users/enable-guests")
    public String enableGuests(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} requested to enable guests", principal.getUser());
        if (systemConfigurationService.isGuestLoginEnabled()) {
            log.warn("Guest login is already enabled");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        systemConfigurationService.setGuestLoginEnabled(true);
        return "redirect:/admin/users";
    }
}
