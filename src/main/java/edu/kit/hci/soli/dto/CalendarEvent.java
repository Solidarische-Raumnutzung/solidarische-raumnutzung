package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Priority;
import edu.kit.hci.soli.domain.ShareRoomType;

import java.time.LocalDateTime;

/**
 * Record representing an event as specified by FullCalendar.
 *
 * @param url             the URL associated with the event
 * @param title           the title of the event
 * @param start           the start date and time of the event
 * @param end             the end date and time of the event
 * @param shareRoomType   the type of room sharing
 * @param favorite        whether the event is owned by the current user
 * @param backgroundColor the background color of the event
 * @param textColor       the text color of the event
 * @param priority        the priority level of the event
 */
public record CalendarEvent(
        String url,
        String title,
        LocalDateTime start,
        LocalDateTime end,
        ShareRoomType shareRoomType,
        boolean favorite,
        String backgroundColor,
        String textColor,
        Priority priority
) {
}
