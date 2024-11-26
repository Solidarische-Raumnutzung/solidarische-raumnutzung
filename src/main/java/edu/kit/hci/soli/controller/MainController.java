package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.repository.VisitsRepository;
import edu.kit.hci.soli.domain.DemoModel;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller("/")
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private VisitsRepository visitsRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    @ResponseBody
    @GetMapping("/profile")
    public String getProfile() {
        return profile;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model, HttpServletResponse response, Principal principal, @AuthenticationPrincipal OidcUser oidcUser) {

        if (oidcUser == null){
            model.addAttribute("model", new DemoModel("", visitsRepository.getVisits()));
            return "index";
        }


        log.info("Received request from {}", principal == null ? "Anonymous" : oidcUser.getUserInfo().getFullName());

        String username = oidcUser.getUserInfo().getFullName();

        model.addAttribute("model", new DemoModel(username, visitsRepository.getVisits()));
        return "index";
    }
}
