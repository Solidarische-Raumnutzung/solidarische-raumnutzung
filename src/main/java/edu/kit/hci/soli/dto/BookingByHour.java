package edu.kit.hci.soli.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing the number of bookings for a specific hour of the day.
 */
public class BookingByHour {
    private int hour;
    private long count;

    /**
     * Constructs a new BookingByHour instance.
     *
     * @param hour the hour of the day (0-23)
     * @param count the number of bookings for the specified hour
     */
    public BookingByHour(int hour, long count) {
        this.hour = hour;
        this.count = count;
    }

    /**
     * Gets the hour of the day.
     *
     * @return the hour of the day
     */
    public int getHour() {
        return this.hour;
    }

    /**
     * Gets the number of bookings for the specified hour.
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
        return o instanceof BookingByHour b && this.getHour() == b.getHour() && this.getCount() == b.getCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getHour(), this.getCount());
    }
}