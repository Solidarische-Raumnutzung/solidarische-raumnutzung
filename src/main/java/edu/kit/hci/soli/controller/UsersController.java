package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

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

        if ("admin".equals(user.getUsername())) {
            return "users";
        }

        userService.toggleUserEnabled(user);
        log.info("User {} deactivated user {}", principal.getUser(), user);

        model.addAttribute("users", userService.getManageableUsers());
        return "users";
    }

    @RequestMapping("/disabled")
    public String getDisabled(Model model) {
        return "disabled_user";
    }

    @GetMapping("/admin/users")
    public String getUsers(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        log.info("User {} requested the users page", principal.getUser());
        model.addAttribute("users", userService.getManageableUsers());
        return "users";
    }
}
