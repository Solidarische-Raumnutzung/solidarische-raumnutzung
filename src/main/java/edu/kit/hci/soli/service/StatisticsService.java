package edu.kit.hci.soli.service;

import java.time.*;
import java.time.temporal.TemporalAmount;
import java.util.Map;

/**
 * Service interface for generating booking statistics.
 */
public interface StatisticsService {
    /**
     * Counts the number of bookings for each day of the week for all time.
     *
     * @return a map where the key is the day of the week and the value is the count of bookings
     */
    Map<DayOfWeek, Long> countBookingsPerWeekdayAllTime();

    /**
     * Counts the number of bookings for each day of the week within a recent time frame.
     *
     * @param frame the temporal amount representing the recent time frame
     * @return a map where the key is the day of the week and the value is the count of bookings
     */
    Map<DayOfWeek, Long> countBookingsPerWeekdayRecent(TemporalAmount frame);

    /**
     * Counts the number of bookings for each hour of the day for all time.
     *
     * @return a map where the key is the hour of the day and the value is the count of bookings
     */
    Map<Integer, Long> countBookingsPerHourAllTime();

    /**
     * Counts the number of bookings for each hour of the day within a recent time frame.
     *
     * @param frame the temporal amount representing the recent time frame
     * @return a map where the key is the hour of the day and the value is the count of bookings
     */
    Map<Integer, Long> countBookingsPerHourRecent(TemporalAmount frame);

    /**
     * Counts the number of bookings for each month of the year for all time.
     *
     * @return a map where the key is the month and the value is the count of bookings
     */
    Map<Month, Long> countBookingsPerMonthAllTime();

    /**
     * Counts the number of bookings for each month of the year within a recent time frame.
     *
     * @param frame the temporal amount representing the recent time frame
     * @return a map where the key is the month and the value is the count of bookings
     */
    Map<Month, Long> countBookingsPerMonthRecent(TemporalAmount frame);
}
