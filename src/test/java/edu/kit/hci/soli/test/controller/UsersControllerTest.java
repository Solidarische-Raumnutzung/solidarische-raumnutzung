package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.UsersController;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.UserService;
import edu.kit.hci.soli.test.TestService;
import io.micrometer.common.lang.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class UsersControllerTest {
    @Autowired private TestService testService;
    @Autowired private UsersController usersController;
    @Autowired private UserService userService;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    private @Nullable KnownError lsmDeactivateUser(Long userId, User principal) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String result = usersController.deactivateUser(model, response, () -> principal, userId);
        if (result.equals("error/known")) {
            return (KnownError) model.get("error");
        }
        assertEquals("redirect:/admin/users", result);
        return null;
    }

    @Test
    public void testDeactivateNonExistentUser() {
        User adminUser = userService.resolveAdminUser();
        assertEquals(KnownError.NOT_FOUND, lsmDeactivateUser(999L, adminUser));
    }

    @Test
    public void testDeactivateAdminUser() {
        User adminUser = userService.resolveAdminUser();
        assertNull(lsmDeactivateUser(adminUser.getId(), adminUser));
    }

    @Test
    public void testDeactivateRegularUser() {
        User adminUser = userService.resolveAdminUser();
        assertNull(lsmDeactivateUser(testService.user.getId(), adminUser));
    }

    @Test
    public void testGetUsersPage() {
        ExtendedModelMap model = new ExtendedModelMap();
        User adminUser = userService.resolveAdminUser();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String result = usersController.getUsers(model, response, () -> adminUser);
        assertEquals("admin/users", result);
    }
}
