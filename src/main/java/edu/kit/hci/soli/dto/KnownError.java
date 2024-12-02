package edu.kit.hci.soli.dto;

import org.jetbrains.annotations.PropertyKey;

public enum KnownError {
    NOT_FOUND("error.404.title", "error.404.message"),
    NO_USER("error.bad_login_method.title", "error.bad_login_method.message"),
    INVALID_TIME("error.invalid_time.title", "error.invalid_time.message"),
    MISSING_PARAMETER("error.missing_parameter.title", "error.missing_parameter.message"),
    EVENT_CONFLICT("error.event_conflict.title", "error.event_conflict.message");

    public final @PropertyKey(resourceBundle = "messages") String title;
    public final @PropertyKey(resourceBundle = "messages") String message;

    KnownError(@PropertyKey(resourceBundle = "messages") String title, @PropertyKey(resourceBundle = "messages") String message) {
        this.title = title;
        this.message = message;
    }
}
