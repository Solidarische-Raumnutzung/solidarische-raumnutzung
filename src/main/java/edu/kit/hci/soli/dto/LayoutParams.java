package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class LayoutParams {
    private final LoginStateModel login;
    private final Consumer<Room> onRoomChange;
    private Room room;
    private @Nullable Booking currentBooking;

    public LayoutParams(@NotNull LoginStateModel login, @Nullable Room room, @NotNull Consumer<@Nullable Room> onRoomChange, @Nullable Booking currentBooking) {
        this.login = Objects.requireNonNull(login);
        this.room = room;
        this.onRoomChange = Objects.requireNonNull(onRoomChange);
        this.currentBooking = currentBooking;
    }

    public @NotNull LoginStateModel getLogin() {
        return this.login;
    }

    public @Nullable Room getRoom() {
        return this.room;
    }

    public void setRoom(@Nullable Room room) {
        this.room = room;
        onRoomChange.accept(room);
    }

    public @Nullable Booking getCurrentBooking() {
        return currentBooking;
    }
}
