package edu.kit.hci.soli.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing the number of bookings for a specific month.
 */
public class BookingByMonth {
    private int month;
    private long count;

    /**
     * Constructs a new BookingByMonth instance.
     *
     * @param month the month of the year (1-12)
     * @param count the number of bookings for the specified month
     */
    public BookingByMonth(int month, long count) {
        this.month = month;
        this.count = count;
    }

    /**
     * Gets the month of the year.
     *
     * @return the month of the year
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Gets the number of bookings for the specified month.
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
        return o instanceof BookingByMonth b && this.getMonth() == b.getMonth() && this.getCount() == b.getCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getMonth(), this.getCount());
    }
}
