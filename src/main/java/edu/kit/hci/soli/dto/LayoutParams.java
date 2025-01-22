package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Room;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class LayoutParams {
    private final LoginStateModel login;
    private final Consumer<Room> onRoomChange;
    private Room room;

    public LayoutParams(@NotNull LoginStateModel login, @Nullable Room room, @NotNull Consumer<@Nullable Room> onRoomChange) {
        this.login = Objects.requireNonNull(login);
        this.room = room;
        this.onRoomChange = Objects.requireNonNull(onRoomChange);
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
}
