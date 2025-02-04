package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.print.Book;
import java.util.Objects;
import java.util.function.Consumer;

public class LayoutParams {
    private final LoginStateModel login;
    private final RoomChangeListener onRoomChange;
    private @Nullable Room room;
    private @Nullable Booking currentHighestBooking;
    private @Nullable Booking currentBookingOfUser;

    public LayoutParams(@NotNull LoginStateModel login, @Nullable Room room, @NotNull RoomChangeListener onRoomChange, @NotNull RoomChangeListener.ParamsUpdate paramsUpdate) {
        this.login = Objects.requireNonNull(login);
        this.room = room;
        this.onRoomChange = Objects.requireNonNull(onRoomChange);
        this.currentHighestBooking = paramsUpdate.currentHighestBooking();
        this.currentBookingOfUser = paramsUpdate.currentBookingOfUser();
    }

    public @NotNull LoginStateModel getLogin() {
        return this.login;
    }

    public @Nullable Room getRoom() {
        return this.room;
    }

    public void setRoom(@Nullable Room room) {
        RoomChangeListener.ParamsUpdate paramsUpdate = onRoomChange.onRoomChange(room);
        this.room = room;
        this.currentHighestBooking = paramsUpdate.currentHighestBooking();
        this.currentBookingOfUser = paramsUpdate.currentBookingOfUser();
    }

    public @Nullable Booking getCurrentHighestBooking() {
        return currentHighestBooking;
    }

    public @Nullable Booking getCurrentBookingOfUser() {
        return currentBookingOfUser;
    }

    public interface RoomChangeListener {
        /**
         * @param room the new room
         * @return the new current highest booking
         */
        @NotNull ParamsUpdate onRoomChange(@Nullable Room room);

        record ParamsUpdate(@Nullable Booking currentHighestBooking, @Nullable Booking currentBookingOfUser) {}
    }
}
