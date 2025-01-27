package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class LayoutParams {
    private final LoginStateModel login;
    private final RoomChangeListener onRoomChange;
    private @Nullable Room room;
    private @Nullable Booking currentHighestBooking;

    public LayoutParams(@NotNull LoginStateModel login, @Nullable Room room, @NotNull RoomChangeListener onRoomChange, @Nullable Booking currentHighestBooking) {
        this.login = Objects.requireNonNull(login);
        this.room = room;
        this.onRoomChange = Objects.requireNonNull(onRoomChange);
        this.currentHighestBooking = currentHighestBooking;
    }

    public @NotNull LoginStateModel getLogin() {
        return this.login;
    }

    public @Nullable Room getRoom() {
        return this.room;
    }

    public void setRoom(@Nullable Room room) {
        this.room = room;
        this.currentHighestBooking = onRoomChange.onRoomChange(room);
    }

    public @Nullable Booking getCurrentHighestBooking() {
        return currentHighestBooking;
    }

    public interface RoomChangeListener {
        /**
         * @param room the new room
         * @return the new current highest booking
         */
        @Nullable Booking onRoomChange(@Nullable Room room);
    }
}
