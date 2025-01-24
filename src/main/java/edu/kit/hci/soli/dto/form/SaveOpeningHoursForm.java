package edu.kit.hci.soli.dto.form;

import edu.kit.hci.soli.domain.TimeTuple;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;

/**
 * A form for saving opening hours for each weekday from Monday to Friday.
 */
public class SaveOpeningHoursForm {
    private LocalTime mondayStart;
    private LocalTime mondayEnd;
    private LocalTime tuesdayStart;
    private LocalTime tuesdayEnd;
    private LocalTime wednesdayStart;
    private LocalTime wednesdayEnd;
    private LocalTime thursdayStart;
    private LocalTime thursdayEnd;
    private LocalTime fridayStart;
    private LocalTime fridayEnd;

    /**
     * Constructs a new SaveOpeningHoursForm with the specified opening and closing times for each weekday.
     *
     * @param mondayStart    the opening time on Monday
     * @param mondayEnd      the closing time on Monday
     * @param tuesdayStart   the opening time on Tuesday
     * @param tuesdayEnd     the closing time on Tuesday
     * @param wednesdayStart the opening time on Wednesday
     * @param wednesdayEnd   the closing time on Wednesday
     * @param thursdayStart  the opening time on Thursday
     * @param thursdayEnd    the closing time on Thursday
     * @param fridayStart    the opening time on Friday
     * @param fridayEnd      the closing time on Friday
     */
    public SaveOpeningHoursForm(LocalTime mondayStart, LocalTime mondayEnd, LocalTime tuesdayStart, LocalTime tuesdayEnd, LocalTime wednesdayStart, LocalTime wednesdayEnd, LocalTime thursdayStart, LocalTime thursdayEnd, LocalTime fridayStart, LocalTime fridayEnd) {
        this.mondayStart = mondayStart;
        this.mondayEnd = mondayEnd;
        this.tuesdayStart = tuesdayStart;
        this.tuesdayEnd = tuesdayEnd;
        this.wednesdayStart = wednesdayStart;
        this.wednesdayEnd = wednesdayEnd;
        this.thursdayStart = thursdayStart;
        this.thursdayEnd = thursdayEnd;
        this.fridayStart = fridayStart;
        this.fridayEnd = fridayEnd;
    }

    /**
     * Gets the opening time on Monday.
     *
     * @return the opening time on Monday
     */
    public LocalTime getMondayStart() {
        return this.mondayStart;
    }

    /**
     * Gets the closing time on Monday.
     *
     * @return the closing time on Monday
     */
    public LocalTime getMondayEnd() {
        return this.mondayEnd;
    }

    /**
     * Gets the opening time on Tuesday.
     *
     * @return the opening time on Tuesday
     */
    public LocalTime getTuesdayStart() {
        return this.tuesdayStart;
    }

    /**
     * Gets the closing time on Tuesday.
     *
     * @return the closing time on Tuesday
     */
    public LocalTime getTuesdayEnd() {
        return this.tuesdayEnd;
    }

    /**
     * Gets the opening time on Wednesday.
     *
     * @return the opening time on Wednesday
     */
    public LocalTime getWednesdayStart() {
        return this.wednesdayStart;
    }

    /**
     * Gets the closing time on Wednesday.
     *
     * @return the closing time on Wednesday
     */
    public LocalTime getWednesdayEnd() {
        return this.wednesdayEnd;
    }

    /**
     * Gets the opening time on Thursday.
     *
     * @return the opening time on Thursday
     */
    public LocalTime getThursdayStart() {
        return this.thursdayStart;
    }

    /**
     * Gets the closing time on Thursday.
     *
     * @return the closing time on Thursday
     */
    public LocalTime getThursdayEnd() {
        return this.thursdayEnd;
    }

    /**
     * Gets the opening time on Friday.
     *
     * @return the opening time on Friday
     */
    public LocalTime getFridayStart() {
        return this.fridayStart;
    }

    /**
     * Gets the closing time on Friday.
     *
     * @return the closing time on Friday
     */
    public LocalTime getFridayEnd() {
        return this.fridayEnd;
    }

    /**
     * Sets the opening time on Monday.
     *
     * @param mondayStart the opening time on Monday
     */
    public void setMondayStart(LocalTime mondayStart) {
        this.mondayStart = mondayStart;
    }

    /**
     * Sets the closing time on Monday.
     *
     * @param mondayEnd the closing time on Monday
     */
    public void setMondayEnd(LocalTime mondayEnd) {
        this.mondayEnd = mondayEnd;
    }

    /**
     * Sets the opening time on Tuesday.
     *
     * @param tuesdayStart the opening time on Tuesday
     */
    public void setTuesdayStart(LocalTime tuesdayStart) {
        this.tuesdayStart = tuesdayStart;
    }

    /**
     * Sets the closing time on Tuesday.
     *
     * @param tuesdayEnd the closing time on Tuesday
     */
    public void setTuesdayEnd(LocalTime tuesdayEnd) {
        this.tuesdayEnd = tuesdayEnd;
    }

    /**
     * Sets the opening time on Wednesday.
     *
     * @param wednesdayStart the opening time on Wednesday
     */
    public void setWednesdayStart(LocalTime wednesdayStart) {
        this.wednesdayStart = wednesdayStart;
    }

    /**
     * Sets the closing time on Wednesday.
     *
     * @param wednesdayEnd the closing time on Wednesday
     */
    public void setWednesdayEnd(LocalTime wednesdayEnd) {
        this.wednesdayEnd = wednesdayEnd;
    }

    /**
     * Sets the opening time on Thursday.
     *
     * @param thursdayStart the opening time on Thursday
     */
    public void setThursdayStart(LocalTime thursdayStart) {
        this.thursdayStart = thursdayStart;
    }

    /**
     * Sets the closing time on Thursday.
     *
     * @param thursdayEnd the closing time on Thursday
     */
    public void setThursdayEnd(LocalTime thursdayEnd) {
        this.thursdayEnd = thursdayEnd;
    }

    /**
     * Sets the opening time on Friday.
     *
     * @param fridayStart the opening time on Friday
     */
    public void setFridayStart(LocalTime fridayStart) {
        this.fridayStart = fridayStart;
    }

    /**
     * Sets the closing time on Friday.
     *
     * @param fridayEnd the closing time on Friday
     */
    public void setFridayEnd(LocalTime fridayEnd) {
        this.fridayEnd = fridayEnd;
    }

    public Map<DayOfWeek, TimeTuple> toMap() {
        return Map.of(
                DayOfWeek.MONDAY, new TimeTuple(mondayStart, mondayEnd),
                DayOfWeek.TUESDAY, new TimeTuple(tuesdayStart, tuesdayEnd),
                DayOfWeek.WEDNESDAY, new TimeTuple(wednesdayStart, wednesdayEnd),
                DayOfWeek.THURSDAY, new TimeTuple(thursdayStart, thursdayEnd),
                DayOfWeek.FRIDAY, new TimeTuple(fridayStart, fridayEnd)
        );
    }
}
