package edu.kit.hci.soli.dto;

import org.jetbrains.annotations.PropertyKey;

/**
 * Enumeration representing known errors with their corresponding titles and messages.
 */
public enum KnownError {
    /**
     * Error indicating that the requested resource was not found.
     */
    NOT_FOUND("error.404.title", "error.404.message"),

    /**
     * Error indicating that the provided time frame is invalid.
     */
    INVALID_TIME("error.invalid_time.title", "error.invalid_time.message"),

    /**
     * Error indicating that a required parameter is missing.
     */
    MISSING_PARAMETER("error.missing_parameter.title", "error.missing_parameter.message"),

    /**
     * Error indicating that the user does not have permission to delete a booking.
     */
    DELETE_NO_PERMISSION("error.delete_no_permission.title", "error.delete_no_permission.message"),

    /**
     * Error indicating that there is an unresolvable conflict with an existing booking.
     */
    EVENT_CONFLICT("error.event_conflict.title", "error.event_conflict.message");

    /**
     * The title of the error message.
     */
    public final @PropertyKey(resourceBundle = "messages") String title;

    /**
     * The detailed error message.
     */
    public final @PropertyKey(resourceBundle = "messages") String message;

    /**
     * Constructs a KnownError with the specified title and message.
     *
     * @param title   the title of the error message
     * @param message the detailed error message
     */
    KnownError(@PropertyKey(resourceBundle = "messages") String title, @PropertyKey(resourceBundle = "messages") String message) {
        this.title = title;
        this.message = message;
    }
}
