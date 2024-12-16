package edu.kit.hci.soli.dto;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Record representing an event as specified by FullCalendar.
 *
 * @param url        the URL associated with the event
 * @param title      the title of the event
 * @param start      the start date and time of the event
 * @param end        the end date and time of the event
 * @param classNames the list of CSS classes to use for the generated HTML element of this event
 */
public record CalendarEvent(String url, String title, ZonedDateTime start, ZonedDateTime end, List<String> classNames) {
}
