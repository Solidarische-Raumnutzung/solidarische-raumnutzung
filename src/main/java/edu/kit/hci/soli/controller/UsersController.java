package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.SoliConfiguration;
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
@Controller("/admin/users")
@Slf4j
public class UsersController {
    private final UserService userService;
    private final SystemConfigurationService systemConfigurationService;
    private final int maxPaginationSize;

    /**
     * Constructs a UsersController with the specified {@link UserService}.
     *
     * @param userService                the service for managing users
     * @param systemConfigurationService the service for managing the system configuration
     * @param soliConfiguration          the configuration of the application
     */
    public UsersController(UserService userService, SystemConfigurationService systemConfigurationService, SoliConfiguration soliConfiguration) {
        this.userService = userService;
        this.systemConfigurationService = systemConfigurationService;
        this.maxPaginationSize = soliConfiguration.getPagination().getMaxSize();
    }

    @GetMapping("/admin/users/{userId}/deactivate")
    public String deactivateUserConfirmation(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal, @PathVariable Long userId) {
        log.info("User {} opened the dialog to deactivate user {}", principal.getUser(), userId);
        User user = userService.getById(userId);

        if (user == null) {
            log.info("User {} not found", userId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        if ("admin".equals(user.getUsername())) {
            return "redirect:/admin/users";
        }

        model.addAttribute("user", user);
        return "admin/confirmation/deactivate_user_page";
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
            return "error/known";
        }

        if (user.isDisabled()) {
            log.info("User {} is already deactivated", user);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
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
            return "error/known";
        }

        if (!user.isDisabled()) {
            log.info("User {} is already active", user);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
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
     * @param page      the page number
     *                  (0-based, i.e., the first page is page 0)
     * @param size      the number of users per page
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @return the view name
     */
    @GetMapping("/admin/users")
    public String getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            HttpServletResponse response,
            @AuthenticationPrincipal SoliUserDetails principal) {

        if (size > maxPaginationSize) {
            size = maxPaginationSize;
        }

        log.info("User {} requested the users page", principal.getUser());
        model.addAttribute("users", userService.getManageableUsers(page, size));
        model.addAttribute("guestsEnabled", systemConfigurationService.isGuestLoginEnabled());
        return "admin/users";
    }

    /**
     * Displays the confirmation dialog for disabling guest login.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @return the view name
     */
    @GetMapping("/admin/users/disable-guests")
    public String disableGuestsConfirmation(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} opened the dialog to disable guest login", principal.getUser());
        if (!systemConfigurationService.isGuestLoginEnabled()) {
            log.warn("Guest login is already disabled");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        return "admin/confirmation/disable_guests_page";
    }

    /**
     * Disables guest login.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @return the view name
     */
    @PutMapping("/admin/users/disable-guests")
    public String disableGuests(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} requested to disable guests", principal.getUser());
        if (!systemConfigurationService.isGuestLoginEnabled()) {
            log.warn("Guest login is already disabled");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        systemConfigurationService.setGuestLoginEnabled(false);
        return "redirect:/admin/users";
    }

    /**
     * Enables guest login.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @return the view name
     */
    @PutMapping("/admin/users/enable-guests")
    public String enableGuests(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} requested to enable guests", principal.getUser());
        if (systemConfigurationService.isGuestLoginEnabled()) {
            log.warn("Guest login is already enabled");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        systemConfigurationService.setGuestLoginEnabled(true);
        return "redirect:/admin/users";
    }
}
