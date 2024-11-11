package edu.kit.hci.soli;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    final VisitsRepository visitsRepository;

    @Value("${spring.profiles.active:PROFILE_NOT_SET")
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
    public String index(Model model, HttpServletResponse response) {
        visitsRepository.increment();

        model.addAttribute("model", new DemoModel("Anon", visitsRepository.getVisits()));
        return "index";
    }
}
