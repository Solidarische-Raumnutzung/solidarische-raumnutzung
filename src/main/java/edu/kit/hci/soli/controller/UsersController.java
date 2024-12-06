package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.BookingsService;
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

    @PutMapping("/admin/users/deactivate/{userId}")
    public String deactivateUser(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails soliUserDetails, @PathVariable Long userId) {
        User user = userService.getById(userId);

        if (user == null) {
            log.info("User {} not found", userId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        userService.disableOrRenableUser(user);

        model.addAttribute("users", userService.getAll());
        return "users";
    }


    @RequestMapping("/disabled")
    public String getDisabled(Model model) {
        return "disabled_user";
    }

    @GetMapping("/admin/users")
    public String getUsers(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails soliUserDetails) {
        log.info("User {} requested the users page", soliUserDetails);
        model.addAttribute("users", userService.getAll());
        return "users";
    }



}
