package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.BookingCreateController;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class BookingCreateControllerTest {
    @Autowired private TestService testService;
    @Autowired private BookingCreateController bookingsController;
    @Autowired private BookingsService bookingsService;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    private @Nullable KnownError lsmCreateBooking(BookingCreateController.FormData formData, User user, long room) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        String result = bookingsController.createBooking(model, response, request, room, () -> user, formData);
        if (result.equals("error_known")) {
            return (KnownError) model.get("error");
        }
        assertEquals("redirect:/1", result);
        return null;
    }

    @Test
    public void testIllegalRoom() {
        BookingCreateController.FormData formData = new BookingCreateController.FormData(
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
        BookingCreateController.FormData formData = new BookingCreateController.FormData(
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
        BookingCreateController.FormData formData = new BookingCreateController.FormData(
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
        BookingCreateController.FormData formData = new BookingCreateController.FormData(
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
        BookingCreateController.FormData formData = new BookingCreateController.FormData(
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
        BookingCreateController.FormData formData = new BookingCreateController.FormData(
                bookingsService.currentSlot().plusMinutes(30),
                bookingsService.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertNull(lsmCreateBooking(formData, testService.user, 1));
    }
}
