package edu.kit.hci.soli.controller;


import edu.kit.hci.soli.domain.LoginStateModel;
import edu.kit.hci.soli.repository.VisitsRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller("/bookings")
public class BookingsController {

    @Autowired
    private VisitsRepository visitsRepository;

    @GetMapping("/bookings")
    public String bookings(Model model, HttpServletResponse response, Principal principal, @AuthenticationPrincipal OidcUser oidcUser) {

        String username = oidcUser.getUserInfo().getFullName();
        model.addAttribute("model", new LoginStateModel(username, visitsRepository.getVisits()));
        return "bookings";
    }
}
