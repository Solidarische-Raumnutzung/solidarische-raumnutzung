package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.SoliApplication;
import edu.kit.hci.soli.controller.BookingsController;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureTestDatabase
@WebMvcTest(BookingsController.class)
@ContextConfiguration(classes= SoliApplication.class)
public class BookingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BookingsController bookingsController;

    @MockitoBean
    private BookingsService bookingsService;

    private User testUser;
    private Booking testBooking;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        userService.create(testUser);

        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setUser(testUser);

        when(bookingsController.currentSlot()).thenReturn(java.time.LocalDateTime.now());

    }

    @Test
    public void testDeleteBookingForbidden() throws Exception {
        User anotherUser = new User();
        anotherUser.setUsername("anotheruser");
        anotherUser.setEmail("anotheruser@example.com");

        testBooking.setUser(anotherUser);
        when(bookingsService.getBookingById(1L)).thenReturn(testBooking);
        when(userService.resolveLoggedInUser(Mockito.any(Principal.class))).thenReturn(testUser);

        MockHttpServletResponse response = mockMvc.perform(delete("/bookings/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void testDeleteBookingSuccess() throws Exception {
        when(bookingsService.getBookingById(1L)).thenReturn(testBooking);
        when(userService.resolveLoggedInUser(Mockito.any(Principal.class))).thenReturn(testUser);

        MockHttpServletResponse response = mockMvc.perform(get("/bookings/delete/1").contentType(MediaType.ALL))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.FOUND.value(), response.getStatus());
        Mockito.verify(bookingsService).delete(testBooking);
    }

    @Test
    public void testDeleteBookingNotFound() throws Exception {
        when(bookingsService.getBookingById(1L)).thenReturn(null);

        MockHttpServletResponse response = mockMvc.perform(delete("/bookings/delete/1")
                        .contentType(MediaType.ALL))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
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
