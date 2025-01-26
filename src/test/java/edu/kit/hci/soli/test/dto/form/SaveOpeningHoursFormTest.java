package edu.kit.hci.soli.test.dto.form;

import edu.kit.hci.soli.domain.TimeTuple;
import edu.kit.hci.soli.dto.form.SaveOpeningHoursForm;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaveOpeningHoursFormTest {
    @Test
    void testMondayStart_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(9, 0), form.getMondayStart());
    }

    @Test
    void testMondayEnd_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(17, 0), form.getMondayEnd());
    }

    @Test
    void testToMap_ReturnsCorrectMap() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        Map<DayOfWeek, TimeTuple> map = form.toMap();
        assertEquals(new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0)), map.get(DayOfWeek.MONDAY));
        assertEquals(new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0)), map.get(DayOfWeek.TUESDAY));
        assertEquals(new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0)), map.get(DayOfWeek.WEDNESDAY));
        assertEquals(new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0)), map.get(DayOfWeek.THURSDAY));
        assertEquals(new TimeTuple(LocalTime.of(9, 0), LocalTime.of(17, 0)), map.get(DayOfWeek.FRIDAY));
    }

    @Test
    void testSetMondayStart_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setMondayStart(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), form.getMondayStart());
    }

    @Test
    void testSetMondayEnd_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setMondayEnd(LocalTime.of(18, 0));
        assertEquals(LocalTime.of(18, 0), form.getMondayEnd());
    }

    @Test
    void testTuesdayStart_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(10, 0), LocalTime.of(18, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(10, 0), form.getTuesdayStart());
    }

    @Test
    void testTuesdayEnd_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(10, 0), LocalTime.of(18, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(18, 0), form.getTuesdayEnd());
    }

    @Test
    void testSetTuesdayStart_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(10, 0), LocalTime.of(18, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setTuesdayStart(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), form.getTuesdayStart());
    }

    @Test
    void testSetTuesdayEnd_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(10, 0), LocalTime.of(18, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setTuesdayEnd(LocalTime.of(19, 0));
        assertEquals(LocalTime.of(19, 0), form.getTuesdayEnd());
    }

    @Test
    void testWednesdayStart_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(11, 0), LocalTime.of(19, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(11, 0), form.getWednesdayStart());
    }

    @Test
    void testWednesdayEnd_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(11, 0), LocalTime.of(19, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(19, 0), form.getWednesdayEnd());
    }

    @Test
    void testSetWednesdayStart_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(11, 0), LocalTime.of(19, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setWednesdayStart(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), form.getWednesdayStart());
    }

    @Test
    void testSetWednesdayEnd_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(11, 0), LocalTime.of(19, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setWednesdayEnd(LocalTime.of(20, 0));
        assertEquals(LocalTime.of(20, 0), form.getWednesdayEnd());
    }

    @Test
    void testThursdayStart_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(12, 0), LocalTime.of(20, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(12, 0), form.getThursdayStart());
    }

    @Test
    void testThursdayEnd_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(12, 0), LocalTime.of(20, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        assertEquals(LocalTime.of(20, 0), form.getThursdayEnd());
    }

    @Test
    void testSetThursdayStart_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(12, 0), LocalTime.of(20, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setThursdayStart(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), form.getThursdayStart());
    }

    @Test
    void testSetThursdayEnd_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(12, 0), LocalTime.of(20, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0)
        );
        form.setThursdayEnd(LocalTime.of(21, 0));
        assertEquals(LocalTime.of(21, 0), form.getThursdayEnd());
    }

    @Test
    void testFridayStart_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(13, 0), LocalTime.of(21, 0)
        );
        assertEquals(LocalTime.of(13, 0), form.getFridayStart());
    }

    @Test
    void testFridayEnd_ReturnsCorrectTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(13, 0), LocalTime.of(21, 0)
        );
        assertEquals(LocalTime.of(21, 0), form.getFridayEnd());
    }

    @Test
    void testSetFridayStart_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(13, 0), LocalTime.of(21, 0)
        );
        form.setFridayStart(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), form.getFridayStart());
    }

    @Test
    void testSetFridayEnd_UpdatesTime() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalTime.of(13, 0), LocalTime.of(21, 0)
        );
        form.setFridayEnd(LocalTime.of(22, 0));
        assertEquals(LocalTime.of(22, 0), form.getFridayEnd());
    }
}