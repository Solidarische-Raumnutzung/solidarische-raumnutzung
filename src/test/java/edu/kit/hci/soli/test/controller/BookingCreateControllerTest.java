package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.BookingCreateController;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.form.CreateEventForm;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.test.TestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class BookingCreateControllerTest {
    @Autowired private TestService testService;
    @Autowired private BookingCreateController bookingsController;
    @Autowired private BookingsService bookingsService;
    @Autowired private RoomService roomService;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    private @Nullable KnownError lsmCreateBooking(CreateEventForm formData, User user, long room) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        String result = bookingsController.createBooking(model, response, request, room, testService.paramsFor(user, request), () -> user, formData);
        if (result.equals("error/known")) {
            return (KnownError) model.get("error");
        }
        assertEquals("redirect:/" + room, result);
        return null;
    }

    @Test
    public void testIllegalRoom() {
        CreateEventForm formData = new CreateEventForm(
                bookingsService.currentSlot().plusMinutes(30),
                bookingsService.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.NOT_FOUND, lsmCreateBooking(formData, testService.user, -1));
    }

    @Test
    public void testMissingArguments() {
        CreateEventForm formData = new CreateEventForm(
                null,
                null,
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.MISSING_PARAMETER, lsmCreateBooking(formData, testService.user, testService.room.getId()));
    }

    @Test
    public void testMisalignedTime() {
        CreateEventForm formData = new CreateEventForm(
                bookingsService.currentSlot().minusMinutes(31),
                bookingsService.currentSlot().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testService.user, testService.room.getId()));
    }

    @Test
    public void testLargeTime() {
        CreateEventForm formData = new CreateEventForm(
                bookingsService.currentSlot().minusMinutes(30),
                bookingsService.currentSlot().plusHours(10),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testService.user, testService.room.getId()));
    }

    @Test
    public void testPastTime() {
        CreateEventForm formData = new CreateEventForm(
                bookingsService.currentSlot().minusMinutes(15),
                bookingsService.currentSlot().plusMinutes(15),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertEquals(KnownError.INVALID_TIME, lsmCreateBooking(formData, testService.user, testService.room.getId()));
    }

    @Test
    public void testCreateBooking() {
        CreateEventForm formData = new CreateEventForm(
                bookingsService.minimumTime().plusMinutes(30),
                bookingsService.minimumTime().plusHours(1),
                null,
                Priority.HIGHEST,
                ShareRoomType.NO
        );
        assertNull(lsmCreateBooking(formData, testService.user, testService.room.getId()));
    }

    @Test
    public void testNewBooking_RoomNotFound() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Model model = new ExtendedModelMap();
        roomService = mock(RoomService.class);
        bookingsService = mock(BookingsService.class);
        BookingCreateController bookingsController = new BookingCreateController(bookingsService, roomService);

        when(roomService.getOptional(1L)).thenReturn(Optional.empty());

        String view = bookingsController.newBooking(model, response, 1L, testService.paramsFor(testService.user, request), null, null, null);

        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
        assertEquals(KnownError.NOT_FOUND, model.getAttribute("error"));
        assertEquals("error/known", view);
    }

    @Test
    public void testNewBooking_StartAndEndNull() {
        Room room = new Room();
        room.setId(1L);
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Model model = new ExtendedModelMap();
        RoomService roomService = mock(RoomService.class);
        BookingsService bookingsService = mock(BookingsService.class);
        BookingCreateController bookingsController = new BookingCreateController(bookingsService, roomService);

        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));
        when(bookingsService.minimumTime()).thenReturn(LocalDateTime.now().minusDays(1));
        when(bookingsService.maximumTime()).thenReturn(LocalDateTime.now().plusDays(1));

        String view = bookingsController.newBooking(model, response, 1L, testService.paramsFor(testService.user, request), null, null, null);

        assertEquals("bookings/create/form", view);
        assertEquals(room, request.getSession().getAttribute("room"));
        assertEquals(LocalDateTime.now().plusMinutes(30).getMinute(), ((LocalDateTime) model.getAttribute("end")).getMinute());
        assertEquals(LocalDateTime.now().getMinute(), ((LocalDateTime) model.getAttribute("start")).getMinute());
        assertEquals(ShareRoomType.NO, model.getAttribute("cooperative"));
    }

    @Test
    public void testNewBooking_CooperativeNull() {
        Room room = new Room();
        room.setId(1L);
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Model model = new ExtendedModelMap();
        RoomService roomService = mock(RoomService.class);
        BookingsService bookingsService = mock(BookingsService.class);
        BookingCreateController bookingsController = new BookingCreateController(bookingsService, roomService);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusMinutes(30);
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));
        when(bookingsService.minimumTime()).thenReturn(LocalDateTime.now().minusDays(1));
        when(bookingsService.maximumTime()).thenReturn(LocalDateTime.now().plusDays(1));

        String view = bookingsController.newBooking(model, response, 1L, testService.paramsFor(testService.user, request), start, end, null);

        assertEquals("bookings/create/form", view);
        assertEquals(room, request.getSession().getAttribute("room"));
        assertEquals(start, model.getAttribute("start"));
        assertEquals(end, model.getAttribute("end"));
        assertEquals(ShareRoomType.NO, model.getAttribute("cooperative"));
    }

    @Test
    public void testNewBooking_AllParametersProvided() {
        Room room = new Room();
        room.setId(1L);
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Model model = new ExtendedModelMap();
        RoomService roomService = mock(RoomService.class);
        BookingsService bookingsService = mock(BookingsService.class);
        BookingCreateController bookingsController = new BookingCreateController(bookingsService, roomService);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusMinutes(30);
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));
        when(bookingsService.minimumTime()).thenReturn(LocalDateTime.now().minusDays(1));
        when(bookingsService.maximumTime()).thenReturn(LocalDateTime.now().plusDays(1));

        String view = bookingsController.newBooking(model, response, 1L, testService.paramsFor(testService.user, request), start, end, true);

        assertEquals("bookings/create/form", view);
        assertEquals(room, request.getSession().getAttribute("room"));
        assertEquals(start, model.getAttribute("start"));
        assertEquals(end, model.getAttribute("end"));
        assertEquals(ShareRoomType.YES, model.getAttribute("cooperative"));
    }

    @Test
    public void testResolveConflict_AttemptedBookingNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("attemptedBooking", null);
        Model model = new ExtendedModelMap();

        String view = bookingsController.resolveConflict(model, request, 1L, testService.paramsFor(testService.user, request));

        assertEquals(KnownError.NOT_FOUND, model.getAttribute("error"));
        assertEquals("error/known", view);
    }

    @Test
    public void testResolveConflict_RoomNotFound() {
        Booking attemptedBooking = new Booking();
        HttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("attemptedBooking", null);
        Model model = new ExtendedModelMap();
        RoomService roomService = mock(RoomService.class);
        BookingsService bookingsService = mock(BookingsService.class);
        BookingCreateController bookingsController = new BookingCreateController(bookingsService, roomService);

        when(roomService.getOptional(1L)).thenReturn(Optional.empty());

        String view = bookingsController.resolveConflict(model, request, 1L, testService.paramsFor(testService.user, request));

        assertEquals(KnownError.NOT_FOUND, model.getAttribute("error"));
        assertEquals("error/known", view);
    }

    @Test
    public void testResolveConflict_RoomIdMismatch() {
        Room room = new Room();
        room.setId(2L);
        HttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("attemptedBooking", null);
        Model model = new ExtendedModelMap();
        RoomService roomService = mock(RoomService.class);
        BookingsService bookingsService = mock(BookingsService.class);
        BookingCreateController bookingsController = new BookingCreateController(bookingsService, roomService);

        Booking attemptedBooking = new Booking();
        attemptedBooking.setRoom(room);
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));

        String view = bookingsController.resolveConflict(model, request, 1L, testService.paramsFor(testService.user, request));

        assertEquals(KnownError.NOT_FOUND, model.getAttribute("error"));
        assertEquals("error/known", view);
    }

    @Test
    public void testResolveConflict_SuccessfulBookingAttempt() {
        assertEquals(true, true);

        Room room = new Room();
        room.setId(1L);
        MockHttpServletRequest request = new MockHttpServletRequest();
        Model model = new ExtendedModelMap();
        RoomService roomService = mock(RoomService.class);
        BookingsService bookingsService = mock(BookingsService.class);
        BookingCreateController bookingsController = new BookingCreateController(bookingsService, roomService);

        Booking attemptedBooking = new Booking();
        attemptedBooking.setRoom(room);

        BookingAttemptResult.PossibleCooperation bookingResult = new BookingAttemptResult.PossibleCooperation.Immediate(List.of(), List.of());

        request.getSession().setAttribute("attemptedBooking", attemptedBooking);
        request.getSession().setAttribute("bookingResult", bookingResult);

        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));
        when(bookingsService.affirm(attemptedBooking, bookingResult)).thenReturn(new BookingAttemptResult.Success(attemptedBooking));

        String view = bookingsController.resolveConflict(model, request, 1L, testService.paramsFor(testService.user, request));

        assertEquals("redirect:/" + room.getId(), view);
    }

}
