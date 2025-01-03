package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.CollaborationRequestController;
import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class CollaborationRequestControllerTest {
    @Autowired private TestService testService;
    @Autowired private CollaborationRequestController bookingsController;
    @Autowired private BookingsService bookingsService;
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired
    private RoomService roomService;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    private Booking requestedBooking;

    @BeforeEach
    public void setUp() {
        Booking conflictBooking = bookingsRepository.save(testService.createBooking(testService.user));
        requestedBooking = testService.createBooking(testService.user2);
        BookingAttemptResult.PossibleCooperation.Deferred deferred = assertInstanceOf(BookingAttemptResult.PossibleCooperation.Deferred.class, bookingsService.attemptToBook(requestedBooking));
        assertIterableEquals(List.of(), deferred.override());
        assertIterableEquals(List.of(), deferred.cooperate());
        assertIterableEquals(List.of(conflictBooking), deferred.contact());
        requestedBooking = assertInstanceOf(BookingAttemptResult.Staged.class, bookingsService.affirm(requestedBooking, deferred)).booking();
    }

    private @Nullable KnownError lsmViewRequest(Booking booking, Room room, User user) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String result = bookingsController.viewCollaborationRequest(model, response, () -> user, room.getId(), booking.getId());
        if (result.equals("error/known")) {
            return (KnownError) model.get("error");
        }
        assertEquals("bookings/collaboration/modal", result);
        return null;
    }

    private @Nullable KnownError lsmAcceptRequest(Booking booking, Room room, User user) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String result = bookingsController.acceptCollaborationRequest(model, response, () -> user, room.getId(), booking.getId());
        if (result.equals("error/known")) {
            return (KnownError) model.get("error");
        }
        assertEquals("redirect:/" + booking.getRoom().getId() + "/bookings/" + booking.getId(), result);
        return null;
    }

    private @Nullable KnownError lsmRejectRequest(Booking booking, Room room, User user) {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String result = bookingsController.rejectCollaborationRequest(model, response, () -> user, room.getId(), booking.getId());
        if (result.equals("error/known")) {
            return (KnownError) model.get("error");
        }
        assertEquals("redirect:/" + booking.getRoom().getId() + "/bookings", result);
        return null;
    }

    @Test
    public void testViewMissingBooking() {
        assertEquals(KnownError.NOT_FOUND, lsmViewRequest(testService.createBooking(testService.user3), testService.room, testService.user));
    }

    @Test
    public void testAcceptMissingBooking() {
        assertEquals(KnownError.NOT_FOUND, lsmAcceptRequest(testService.createBooking(testService.user3), testService.room, testService.user));
    }

    @Test
    public void testRejectMissingBooking() {
        assertEquals(KnownError.NOT_FOUND, lsmRejectRequest(testService.createBooking(testService.user3), testService.room, testService.user));
    }

    @Test
    public void testViewWrongRoom() {
        Room wrongRoom = new Room();
        wrongRoom.setId(2L);
        assertEquals(KnownError.NOT_FOUND, lsmViewRequest(requestedBooking, wrongRoom, testService.user));
    }

    @Test
    public void testAcceptWrongRoom() {
        Room wrongRoom = new Room();
        wrongRoom.setId(2L);
        assertEquals(KnownError.NOT_FOUND, lsmAcceptRequest(requestedBooking, wrongRoom, testService.user));
    }

    @Test
    public void testRejectWrongRoom() {
        Room wrongRoom = new Room();
        wrongRoom.setId(2L);
        assertEquals(KnownError.NOT_FOUND, lsmRejectRequest(requestedBooking, wrongRoom, testService.user));
    }

    @Test
    public void testViewWrongUser() {
        assertEquals(KnownError.NOT_FOUND, lsmViewRequest(requestedBooking, testService.room, testService.user3));
    }

    @Test
    public void testAcceptWrongUser() {
        assertEquals(KnownError.NOT_FOUND, lsmAcceptRequest(requestedBooking, testService.room, testService.user3));
    }

    @Test
    public void testRejectWrongUser() {
        assertEquals(KnownError.NOT_FOUND, lsmRejectRequest(requestedBooking, testService.room, testService.user3));
    }

    @Test
    public void testViewSuccess() {
        assertNull(lsmViewRequest(requestedBooking, testService.room, testService.user));
        assertIterableEquals(List.of(testService.user), requestedBooking.getOpenRequests());
    }

    @Test
    public void testAcceptSuccess() {
        assertNull(lsmAcceptRequest(requestedBooking, testService.room, testService.user));
        requestedBooking = bookingsService.getBookingById(requestedBooking.getId());
        assertIterableEquals(List.of(), requestedBooking.getOpenRequests());
        assertTrue(roomService.existsById(testService.room.getId()));
    }

    @Test
    public void testRejectSuccess() {
        assertNull(lsmRejectRequest(requestedBooking, testService.room, testService.user));
        requestedBooking = bookingsService.getBookingById(requestedBooking.getId());
        assertNull(requestedBooking);
    }
}
