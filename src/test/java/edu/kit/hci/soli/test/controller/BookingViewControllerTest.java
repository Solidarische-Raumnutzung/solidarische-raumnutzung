package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.controller.BookingViewController;
import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookingViewControllerTest {

    private BookingsService bookingsService;
    private RoomService roomService;
    private UserService userService;
    private BookingViewController bookingViewController;
    private Model model;
    private HttpServletResponse response;
    private SoliUserDetails principal;

    @BeforeEach
    public void setUp() {
        bookingsService = mock(BookingsService.class);
        roomService = mock(RoomService.class);
        userService = mock(UserService.class);
        bookingViewController = new BookingViewController(bookingsService, roomService, userService);
        model = mock(Model.class);
        response = mock(HttpServletResponse.class);
        principal = mock(SoliUserDetails.class);
    }

    @Test
    public void testDeleteBookings_BookingNotFound() throws Exception {
        when(bookingsService.getBookingById(1L)).thenReturn(null);

        String view = bookingViewController.deleteBookings(model, response, principal, 1L, 1L);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(model).addAttribute("error", KnownError.NOT_FOUND);
        assertEquals("error/known", view);
    }

    @Test
    public void testDeleteBookings_BookingNotInRoom() throws Exception {
        Booking booking = new Booking();
        Room room = new Room();
        room.setId(2L);
        booking.setRoom(room);
        when(bookingsService.getBookingById(1L)).thenReturn(booking);

        String view = bookingViewController.deleteBookings(model, response, principal, 1L, 1L);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(model).addAttribute("error", KnownError.NOT_FOUND);
        assertEquals("error/known", view);
    }

    @Test
    public void testDeleteBookings_AdminDeletesBooking() throws Exception {
        Booking booking = new Booking();
        Room room = new Room();
        room.setId(1L);
        booking.setRoom(room);
        User admin = new User();
        admin.setId(1L);
        when(bookingsService.getBookingById(1L)).thenReturn(booking);
        when(userService.resolveAdminUser()).thenReturn(admin);
        when(principal.getUser()).thenReturn(admin);

        String view = bookingViewController.deleteBookings(model, response, principal, 1L, 1L);

        verify(bookingsService).delete(booking, BookingDeleteReason.ADMIN);
        assertEquals("redirect:/1/bookings", view);
    }

    @Test
    public void testRoomBookings_RoomNotFound() throws Exception {
        when(roomService.getOptional(1L)).thenReturn(Optional.empty());

        String view = bookingViewController.roomBookings(model, response, principal, 1L);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(model).addAttribute("error", KnownError.NOT_FOUND);
        assertEquals("error/known", view);
    }

    @Test
    public void testRoomBookings_RoomFound() throws Exception {
        Room room = new Room();
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));
        User user = new User();
        when(principal.getUser()).thenReturn(user);

        String view = bookingViewController.roomBookings(model, response, principal, 1L);

        verify(model).addAttribute("room", room);
        verify(model).addAttribute("bookings", bookingsService.getBookingsByUser(user, room));
        assertEquals("bookings/list", view);
    }

    @Test
    public void testViewEvent_BookingNotFound() throws Exception {
        Room room = new Room();
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));
        when(bookingsService.getBookingById(1L)).thenReturn(null);

        String view = bookingViewController.viewEvent(model, response, principal, 1L, 1L);

        verify(model).addAttribute("error", KnownError.NOT_FOUND);
        assertEquals("error/known", view);
    }

    @Test
    public void testViewEvent_BookingFound() throws Exception {
        Room room = new Room();
        Booking booking = new Booking();
        booking.setRoom(room);
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));
        when(bookingsService.getBookingById(1L)).thenReturn(booking);
        User admin = new User();
        admin.setId(1L);
        when(userService.resolveAdminUser()).thenReturn(admin);
        User user = new User();
        user.setId(2L);
        booking.setUser(user);
        when(principal.getUser()).thenReturn(user);

        String view = bookingViewController.viewEvent(model, response, principal, 1L, 1L);

        verify(model).addAttribute("room", room);
        verify(model).addAttribute("booking", booking);
        assertEquals("bookings/single_page", view);
    }
}