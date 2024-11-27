package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.LoginStateModel;
import edu.kit.hci.soli.repository.VisitsRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller("/")
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);

    @Value("${spring.profiles.active}")
    private String profile;

    @ResponseBody
    @GetMapping("/profile")
    public String getProfile() {
        return profile;
    }

    @GetMapping("/")
    public String index(@ModelAttribute("login") LoginStateModel login) {
        if (!login.name.equals("")) log.info("Received request from {}", login.name);
        return "index";
    }
}
