package edu.kit.hci.soli;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class DemoController {

    private final Logger log = LoggerFactory.getLogger(DemoController.class);

    final VisitsRepository visitsRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    public DemoController(@Autowired VisitsRepository visitsRepository) {
        this.visitsRepository = visitsRepository;
    }

    @ResponseBody
    @GetMapping("/profile")
    public String getProfile() {
        return profile;
    }

    @GetMapping("/")
    public String index(Model model, HttpServletResponse response, Principal principal) {
        visitsRepository.increment();
        log.info("Received request from {}", principal == null ? "Anonymous" : principal.getName());
        String username = principal == null ? "Anonymous" : principal.getName();
        model.addAttribute("model", new DemoModel(username, visitsRepository.getVisits()));
        return "index";
    }
}
