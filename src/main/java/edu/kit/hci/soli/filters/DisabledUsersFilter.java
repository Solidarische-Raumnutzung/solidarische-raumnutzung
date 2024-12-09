package edu.kit.hci.soli.filters;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
@Slf4j
public class DisabledUsersFilter extends OncePerRequestFilter {
    private final UserService userService;

    public DisabledUsersFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getUserPrincipal() == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals("/disabled")) {
            filterChain.doFilter(request, response);
            return;
        }

        // The principal is preserved throughout the session, but we always need to check latest user status
        User user = userService.findByUserId(request.getUserPrincipal().getName());

        if (user != null && user.isDisabled()) {
            log.info("Redirecting disabled user: {}", user);
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/disabled");
                log.info("Redirect completed for disabled user {}", user);
            }
            return;
        }
        filterChain.doFilter(request, response);
    }
}
