package edu.kit.hci.soli.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing the number of bookings for a specific day of the week.
 */
public class BookingByDay {
    private int dayOfWeek;
    private long count;

    /**
     * Constructs a new BookingByDay instance.
     *
     * @param dayOfWeek the day of the week (1 for Monday, 7 for Sunday)
     * @param count the number of bookings for the specified day of the week
     */
    public BookingByDay(int dayOfWeek, long count) {
        this.dayOfWeek = dayOfWeek;
        this.count = count;
    }

    /**
     * Gets the day of the week as Sunday (0) to Saturday (6).
     *
     * @return the day of the week
     */
    public int getDayOfWeek() {
        return this.dayOfWeek;
    }

    /**
     * Gets the number of bookings for the specified day of the week.
     *
     * @return the number of bookings
     */
    public long getCount() {
        return this.count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return o instanceof BookingByDay b && this.getDayOfWeek() == b.getDayOfWeek() && this.getCount() == b.getCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getDayOfWeek(), this.getCount());
    }
}
