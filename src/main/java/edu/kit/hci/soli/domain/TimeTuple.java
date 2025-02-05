package edu.kit.hci.soli.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalTime;
import java.util.Objects;

/**
 * A tuple representing the start and end times.
 */
@Embeddable
public class TimeTuple {
    /**
     * The start time.
     */
    @Column(name = "start_time")
    private LocalTime start;

    /**
     * The end time.
     */
    @Column(name = "end_time")
    private LocalTime end;

    /**
     * Default constructor.
     */
    public TimeTuple() {
        start = LocalTime.of(0, 0);
        end = LocalTime.of(23, 45);
    }

    public TimeTuple(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public LocalTime getStart() {
        return this.start;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public LocalTime getEnd() {
        return this.end;
    }

    /**
     * Sets the start time.
     *
     * @param start the start time
     */
    public void setStart(LocalTime start) {
        this.start = start;
    }

    /**
     * Sets the end time.
     *
     * @param end the end time
     */
    public void setEnd(LocalTime end) {
        this.end = end;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
        return o instanceof TimeTuple t
                && Objects.equals(getStart(), t.getStart())
                && Objects.equals(getEnd(), t.getEnd());
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return Objects.hash(start, end);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "TimeTuple(start=" + this.getStart() + ", end=" + this.getEnd() + ")";
    }
}
