package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.BookingsController;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
public class BookingsControllerTest {
    @Autowired private UserService userService;
    @Autowired private BookingsController bookingsController;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        userService.create(testUser);
    }

    private @Nullable KnownError lsmCreateBooking(BookingsController.FormData formData, User user, long room) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        LoginStateModel lsm = new LoginStateModel("testuser", LoginStateModel.Kind.OAUTH, null, user);
        String result = bookingsController.createBooking(model, response, room, lsm, formData);
        if (result.equals("error_known")) {
            return (KnownError) model.get("error");
        }
        assertEquals("redirect:/1/bookings", result);
        return null;
    }

    @Test
    public void testIllegalUser() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsController.currentSlot().plusMinutes(30),
                bookingsController.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.NO_USER, lsmCreateBooking(formData, null, 1));
    }

    @Test
    public void testIllegalRoom() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsController.currentSlot().plusMinutes(30),
                bookingsController.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.NOT_FOUND, lsmCreateBooking(formData, testUser, 2));
    }

    @Test
    public void testMissingArguments() {
        BookingsController.FormData formData = new BookingsController.FormData(
                null,
                null,
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.MISSING_PARAMETER, lsmCreateBooking(formData, testUser, 1));
    }

    @Test
    public void testMisalignedTime() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsController.currentSlot().minusMinutes(31),
                bookingsController.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testUser, 1));
    }

    @Test
    public void testLargeTime() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsController.currentSlot().minusMinutes(30),
                bookingsController.currentSlot().plusHours(10),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testUser, 1));
    }

    @Test
    public void testPastTime() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsController.currentSlot().minusMinutes(-15),
                bookingsController.currentSlot().plusMinutes(15),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testUser, 1));
    }

    @Test
    public void testCreateBooking() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsController.currentSlot().plusMinutes(30),
                bookingsController.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertNull(lsmCreateBooking(formData, testUser, 1));
    }
}
