package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.BookingsController;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ExtendedModelMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
public class BookingsControllerTest {
    @Autowired private TestService testService;
    @Autowired private BookingsController bookingsController;
    @Autowired private BookingsService bookingsService;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    private @Nullable KnownError lsmCreateBooking(BookingsController.FormData formData, User user, long room) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        LoginStateModel lsm = new LoginStateModel("testuser", LoginStateModel.Kind.OAUTH, null, user);
        String result = bookingsController.createBooking(model, response, request, room, lsm, formData);
        if (result.equals("error_known")) {
            return (KnownError) model.get("error");
        }
        assertThat(result).matches("redirect:/1/bookings/\\d+");
        return null;
    }

    @Test
    public void testIllegalUser() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsService.currentSlot().plusMinutes(30),
                bookingsService.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.NO_USER, lsmCreateBooking(formData, null, 1));
    }

    @Test
    public void testIllegalRoom() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsService.currentSlot().plusMinutes(30),
                bookingsService.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.NOT_FOUND, lsmCreateBooking(formData, testService.user, 2));
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
        assertEquals(KnownError.MISSING_PARAMETER, lsmCreateBooking(formData, testService.user, 1));
    }

    @Test
    public void testMisalignedTime() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsService.currentSlot().minusMinutes(31),
                bookingsService.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testService.user, 1));
    }

    @Test
    public void testLargeTime() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsService.currentSlot().minusMinutes(30),
                bookingsService.currentSlot().plusHours(10),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testService.user, 1));
    }

    @Test
    public void testPastTime() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsService.currentSlot().minusMinutes(15),
                bookingsService.currentSlot().plusMinutes(15),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testService.user, 1));
    }

    @Test
    public void testCreateBooking() {
        BookingsController.FormData formData = new BookingsController.FormData(
                bookingsService.currentSlot().plusMinutes(30),
                bookingsService.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertNull(lsmCreateBooking(formData, testService.user, 1));
    }
}
