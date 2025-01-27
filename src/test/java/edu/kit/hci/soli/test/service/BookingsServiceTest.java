package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.TimeService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class BookingsServiceTest {
    @Autowired private BookingsService bookingsService;
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private TestService testService;
    @Autowired private TimeService timeService;

    private Booking testBooking;
    private Booking testBooking2;
    private Booking testBooking3;

    @BeforeEach
    public void setUp() {
        testService.reset();
        testBooking = testService.createBooking(testService.user);
        testBooking2 = testService.createBooking(testService.user2);
        testBooking3 = testService.createBooking(testService.user3);
    }

    @AfterAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @Test
    public void testGetBookingsByUser() {
        bookingsRepository.save(testBooking);
        List<Booking> bookings = bookingsService.getBookingsByUser(testService.user, testService.room, 0, 10).getContent();
        assertEquals(1, bookings.size());
        assertEquals(testBooking.getId(), bookings.getFirst().getId());
    }

    @Test
    public void testAttemptToBookBookingSuccess() {
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking);
        assertInstanceOf(BookingAttemptResult.Success.class, result);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
    }

    @Test
    public void testAttemptToBookFailure() {
        testBooking.setShareRoomType(ShareRoomType.NO);
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        assertIterableEquals(
                List.of(testBooking),
                assertInstanceOf(BookingAttemptResult.Failure.class, result).conflict()
        );
        assertNull(testBooking2.getId());
    }

    @Test
    public void testAttemptToBookPossibleCooperationImmediate() {
        testBooking.setShareRoomType(ShareRoomType.YES);
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Immediate immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Immediate.class,
                result
        );
        assertIterableEquals(List.of(), immediate.override());
        assertIterableEquals(List.of(testBooking), immediate.cooperate());
        assertNull(testBooking2.getId());

        result = bookingsService.affirm(testBooking2, immediate);
        assertInstanceOf(BookingAttemptResult.Success.class, result);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
        assertTrue(bookingsRepository.existsById(testBooking2.getId()));
    }

    @Test
    public void testAttemptToBookPossibleCooperationDeferred() {
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        assertIterableEquals(List.of(), immediate.override());
        assertIterableEquals(List.of(), immediate.cooperate());
        assertIterableEquals(List.of(testBooking), immediate.contact());
        assertNull(testBooking2.getId());
    }

    @Test
    public void testAttemptToBookPossibleCooperationDeferredOverride() {
        testBooking.setShareRoomType(ShareRoomType.ON_REQUEST);
        testBooking.setPriority(Priority.LOWEST);
        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking3);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        assertIterableEquals(List.of(testBooking), immediate.override());
        assertIterableEquals(List.of(), immediate.cooperate());
        assertIterableEquals(List.of(testBooking2), immediate.contact());
        assertNull(testBooking3.getId());
    }

    @Test
    public void testConfirmRequestSingle() {
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        result = bookingsService.affirm(testBooking2, immediate);
        testBooking2 = assertInstanceOf(BookingAttemptResult.Staged.class, result).booking();
        assertIterableEquals(List.of(testService.user), testBooking2.getOpenRequests());
        assertFalse(bookingsService.confirmRequest(testBooking2, testService.user2));
        assertIterableEquals(List.of(testService.user), testBooking2.getOpenRequests());
        assertTrue(bookingsService.confirmRequest(testBooking2, testService.user));
        assertIterableEquals(List.of(), testBooking2.getOpenRequests());
    }

    @Test
    public void testConfirmRequestMultiple() {
        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking3);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        result = bookingsService.affirm(testBooking3, immediate);
        testBooking3 = assertInstanceOf(BookingAttemptResult.Staged.class, result).booking();
        assertEquals(Set.of(testService.user, testService.user2), testBooking3.getOpenRequests());
        assertFalse(bookingsService.confirmRequest(testBooking3, testService.user3));
        assertEquals(Set.of(testService.user, testService.user2), testBooking3.getOpenRequests());
        assertTrue(bookingsService.confirmRequest(testBooking3, testService.user));
        assertEquals(Set.of(testService.user2), testBooking3.getOpenRequests());
        assertTrue(bookingsService.confirmRequest(testBooking3, testService.user2));
        assertEquals(Set.of(), testBooking3.getOpenRequests());
    }

    @Test
    public void testDenyRequest() {
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        result = bookingsService.affirm(testBooking2, immediate);
        testBooking2 = assertInstanceOf(BookingAttemptResult.Staged.class, result).booking();
        assertIterableEquals(List.of(testService.user), testBooking2.getOpenRequests());
        assertFalse(bookingsService.denyRequest(testBooking2, testService.user2));
        assertIterableEquals(List.of(testService.user), testBooking2.getOpenRequests());
        assertNotNull(bookingsService.getBookingById(testBooking2.getId()));
        assertTrue(bookingsService.denyRequest(testBooking2, testService.user));
        assertIterableEquals(List.of(), testBooking2.getOpenRequests());
        assertFalse(bookingsService.denyRequest(testBooking2, testService.user));
        assertNull(bookingsService.getBookingById(testBooking2.getId()));
    }

    @Test
    public void testDenyRequestIllegal() {
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        result = bookingsService.affirm(testBooking2, immediate);
        testBooking2 = assertInstanceOf(BookingAttemptResult.Staged.class, result).booking();
        assertFalse(bookingsService.denyRequest(testBooking2, testService.user2));
    }

    @Test
    public void testAffirmIllegal() {
        testBooking.setShareRoomType(ShareRoomType.YES);
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Immediate immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Immediate.class,
                result
        );
        result = bookingsService.affirm(testBooking2, immediate);
        testBooking2 = assertInstanceOf(BookingAttemptResult.Success.class, result).booking();
        assertThrows(IllegalArgumentException.class, () -> bookingsService.affirm(testBooking2, immediate));
    }

    @Test
    public void testDelete() {
        testBooking = bookingsRepository.save(testBooking);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
        bookingsService.delete(testBooking, BookingDeleteReason.ADMIN);
        assertFalse(bookingsRepository.existsById(testBooking.getId()));
    }

    @Test
    public void testGetCalendarEvents() {
        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        testBooking3 = bookingsRepository.save(testBooking3);
        List<CalendarEvent> events = bookingsService.getCalendarEvents(testService.room, timeService.currentSlot(), timeService.currentSlot().plusDays(3), null);
        assertEquals(3, events.size());
        assertEquals(testBooking.getStartDate(), events.get(0).start());
        assertEquals(testBooking.getEndDate(), events.get(0).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(0).classNames());
        assertEquals(testBooking2.getStartDate(), events.get(1).start());
        assertEquals(testBooking2.getEndDate(), events.get(1).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(1).classNames());
        assertEquals(testBooking3.getStartDate(), events.get(2).start());
        assertEquals(testBooking3.getEndDate(), events.get(2).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(2).classNames());
    }

    @Test
    public void testGetCalendarEventsAs() {
        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        testBooking3 = bookingsRepository.save(testBooking3);
        List<CalendarEvent> events = bookingsService.getCalendarEvents(testService.room, timeService.currentSlot(), timeService.currentSlot().plusDays(3), testService.user);
        assertEquals(3, events.size());
        assertEquals(testBooking.getStartDate(), events.get(0).start());
        assertEquals(testBooking.getEndDate(), events.get(0).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request", "calendar-event-own"), events.get(0).classNames());
        assertEquals(testBooking2.getStartDate(), events.get(1).start());
        assertEquals(testBooking2.getEndDate(), events.get(1).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(1).classNames());
        assertEquals(testBooking3.getStartDate(), events.get(2).start());
        assertEquals(testBooking3.getEndDate(), events.get(2).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(2).classNames());
    }

    @Test
    public void testUpdateEventDescription() {
        testBooking.setDescription("test description");
        testBooking = bookingsRepository.save(testBooking);
        assertEquals("test description", bookingsService.getBookingById(testBooking.getId()).getDescription());
        assertEquals("test description", testBooking.getDescription());
        bookingsService.updateDescription(testBooking, "new description");
        assertEquals("new description", bookingsService.getBookingById(testBooking.getId()).getDescription());
        assertEquals("new description", testBooking.getDescription());
    }

    @Test
    public void testUpdateEventDescriptionMissing() {
        testBooking.setDescription("test description");
        assertEquals("test description", testBooking.getDescription());
        assertThrows(IllegalArgumentException.class, () -> bookingsService.updateDescription(testBooking, "new description"));
        assertEquals("test description", testBooking.getDescription());
    }

    @Test
    public void testUpdateEventDescriptionWrongId() {
        testBooking.setDescription("test description");
        testBooking = bookingsRepository.save(testBooking);
        testBooking.setId(testBooking.getId() + 1);
        assertEquals("test description", testBooking.getDescription());
        assertThrows(IllegalArgumentException.class, () -> bookingsService.updateDescription(testBooking, "new description"));
        assertEquals("test description", testBooking.getDescription());
    }

    @Test
    public void testGetICalendar() {
        testBooking.setDescription("test description\n very good:!");
        testBooking.setStartDate(LocalDateTime.of(2025, 1, 12, 5, 0));
        testBooking.setEndDate(LocalDateTime.of(2025, 1, 12, 6, 0));
        testBooking = bookingsRepository.save(testBooking);
        var ical = bookingsService.getICalendar(testBooking, Locale.ENGLISH);
        assertEquals("""
                BEGIN:VCALENDAR
                VERSION:2.0
                PRODID:-//HCI SOLI//NONSGML HCI Solidarische Raumnutzung//EN
                BEGIN:VEVENT
                UID:4e58d14a-3926-6471-""".replace("\n", "\r\n"), ical.split("0000-")[0]);
        assertEquals("""
                :20250112T040000Z
                DTEND:20250112T050000Z
                SUMMARY:SOLI-Buchung
                DESCRIPTION:test description\\n
                  very good\\:!
                URL:http://localhost""".replace("\n", "\r\n"), ical.split("DTSTART")[1].split(":8080/")[0]);
        assertTrue(ical.endsWith("""
                LOCATION:Testort
                END:VEVENT
                END:VCALENDAR
                """.replace("\n", "\r\n")));
    }
}