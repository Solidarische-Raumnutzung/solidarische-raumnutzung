package edu.kit.hci.soli.test.filter;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.filter.DisabledUsersFilter;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class DisabledUsersFilterTest {

    private UserService userService;
    private DisabledUsersFilter filter;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        filter = new DisabledUsersFilter(userService);
    }

    @Test
    public void testDoFilterInternal_UserNotLoggedIn() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_UserDisabled() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/someUri");
        request.setUserPrincipal(() -> "user1");
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        User user = new User();
        user.setDisabled(true);
        when(userService.findByUserId("user1")).thenReturn(user);

        filter.doFilter(request, response, filterChain);

        verify(response, times(1)).sendRedirect(request.getContextPath() + "/disabled");
        verify(filterChain, times(0)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_UserEnabled() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/someUri");
        request.setUserPrincipal(() -> "user1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        User user = new User();
        user.setDisabled(false);
        when(userService.findByUserId("user1")).thenReturn(user);

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_DisabledUri() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/disabled");
        request.setUserPrincipal(() -> "user1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
