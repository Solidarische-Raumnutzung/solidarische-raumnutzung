package edu.kit.hci.soli.test.domain;

import edu.kit.hci.soli.domain.TimeTuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeTupleTest {

    @Test
    void testGetStart_ReturnsCorrectStartTime() {
        TimeTuple timeTuple = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        assertEquals(LocalTime.of(9, 0), timeTuple.getStart());
    }

    @Test
    void testGetEnd_ReturnsCorrectEndTime() {
        TimeTuple timeTuple = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        assertEquals(LocalTime.of(17, 0), timeTuple.getEnd());
    }

    @Test
    void testSetStart_UpdatesStartTime() {
        TimeTuple timeTuple = new TimeTuple();
        timeTuple.setStart(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), timeTuple.getStart());
    }

    @Test
    void testSetEnd_UpdatesEndTime() {
        TimeTuple timeTuple = new TimeTuple();
        timeTuple.setEnd(LocalTime.of(18, 0));
        assertEquals(LocalTime.of(18, 0), timeTuple.getEnd());
    }

    @Test
    void testEquals_SameStartAndEndTimes_ReturnsTrue() {
        TimeTuple timeTuple1 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        TimeTuple timeTuple2 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        assertTrue(timeTuple1.equals(timeTuple2));
    }

    @Test
    void testEquals_DifferentStartTimes_ReturnsFalse() {
        TimeTuple timeTuple1 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        TimeTuple timeTuple2 = new TimeTuple(LocalTime.of(8, 0), LocalTime.of(17, 0));
        assertFalse(timeTuple1.equals(timeTuple2));
    }

    @Test
    void testEquals_DifferentEndTimes_ReturnsFalse() {
        TimeTuple timeTuple1 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        TimeTuple timeTuple2 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(18, 0));
        assertFalse(timeTuple1.equals(timeTuple2));
    }

    @Test
    void testEquals_Null_ReturnsFalse() {
        TimeTuple timeTuple = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        assertFalse(timeTuple.equals(null));
    }

    @Test
    void testHashCode_SameStartAndEndTimes_SameHashCode() {
        TimeTuple timeTuple1 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        TimeTuple timeTuple2 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        assertEquals(timeTuple1.hashCode(), timeTuple2.hashCode());
    }

    @Test
    void testHashCode_DifferentStartTimes_DifferentHashCode() {
        TimeTuple timeTuple1 = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        TimeTuple timeTuple2 = new TimeTuple(LocalTime.of(8, 0), LocalTime.of(17, 0));
        assertNotEquals(timeTuple1.hashCode(), timeTuple2.hashCode());
    }

    @Test
    void testToString_ReturnsCorrectStringRepresentation() {
        TimeTuple timeTuple = new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0));
        assertEquals("TimeTuple(start=09:00, end=17:00)", timeTuple.toString());
    }
}