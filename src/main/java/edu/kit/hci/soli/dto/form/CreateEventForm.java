package edu.kit.hci.soli.dto.form;

import edu.kit.hci.soli.domain.Priority;
import edu.kit.hci.soli.domain.ShareRoomType;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Data class for event creation form data.
 */
public class CreateEventForm {
    private LocalDateTime start;
    private LocalTime end;
    private String description;
    private Priority priority;
    private ShareRoomType cooperative;

    /**
     * Constructs a new CreateEventForm object with the specified parameters.
     *
     * @param start        the start time and date of the event
     * @param end          the end time of the event
     * @param description  a brief description of the event
     * @param priority     the priority level of the event
     * @param cooperative  the cooperative (shared room) type of the event
     */
    public CreateEventForm(LocalDateTime start, LocalTime end, String description, Priority priority, ShareRoomType cooperative) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.priority = priority;
        this.cooperative = cooperative;
    }

    /**
     * Returns the start time of the event.
     *
     * @return the start time of the event
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     * Returns the end time of the event.
     *
     * @return the end time of the event
     */
    public LocalTime getEnd() {
        return this.end;
    }

    /**
     * Returns the description of the event.
     *
     * @return the description of the event
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the priority of the event.
     *
     * @return the priority of the event
     */
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * Returns the cooperative type of the event.
     *
     * @return the cooperative type of the event
     */
    public ShareRoomType getCooperative() {
        return this.cooperative;
    }

    /**
     * Sets the start time of the event.
     *
     * @param start the start time to set
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Sets the end time of the event.
     *
     * @param end the end time to set
     */
    public void setEnd(LocalTime end) {
        this.end = end;
    }

    /**
     * Sets the description of the event.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the priority of the event.
     *
     * @param priority the priority to set
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Sets the cooperative type of the event.
     *
     * @param cooperative the cooperative type to set
     */
    public void setCooperative(ShareRoomType cooperative) {
        this.cooperative = cooperative;
    }
}
