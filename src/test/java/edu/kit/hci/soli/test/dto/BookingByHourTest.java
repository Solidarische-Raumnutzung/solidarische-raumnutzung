package edu.kit.hci.soli.test.dto;

import edu.kit.hci.soli.dto.BookingByHour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BookingByHourTest {
    @Test
    void testGetBookingByHourHour() {
        BookingByHour booking = new BookingByHour(3, 10);
        assertEquals(3, booking.getHour());
    }

    @Test
    void testGetBookingByHourCount() {
        BookingByHour booking = new BookingByHour(3, 10);
        assertEquals(10, booking.getCount());
    }

    @Test
    void testBookingByHourDayEqualsIsEqual() {
        BookingByHour booking = new BookingByHour(3, 10);
        BookingByHour booking2 = new BookingByHour(3, 10);
        assertTrue(booking.equals(booking2));
    }

    @Test
    void testBookingByHourDayEqualsDifferentHour() {
        BookingByHour booking = new BookingByHour(1, 10);
        BookingByHour booking2 = new BookingByHour(3, 10);
        assertFalse(booking.equals(booking2));
    }

    @Test
    void testBookingByHourDayEqualsDifferentCount() {
        BookingByHour booking = new BookingByHour(1, 10);
        BookingByHour booking2 = new BookingByHour(3, 9);
        assertFalse(booking.equals(booking2));
    }

    @Test
    void testBookingByHourDayHashIsEqual() {
        BookingByHour booking = new BookingByHour(3, 10);
        BookingByHour booking2 = new BookingByHour(3, 10);
        assertEquals(booking.hashCode(), booking2.hashCode());
    }

    @Test
    void testBookingByHourDayHashDifferentHour() {
        BookingByHour booking = new BookingByHour(1, 10);
        BookingByHour booking2 = new BookingByHour(3, 10);
        assertNotEquals(booking.hashCode(), booking2.hashCode());
    }

    @Test
    void testBookingByHourDayHashDifferentCount() {
        BookingByHour booking = new BookingByHour(1, 10);
        BookingByHour booking2 = new BookingByHour(3, 9);
        assertNotEquals(booking.hashCode(), booking2.hashCode());
    }
}
