package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Room;

import java.util.function.Consumer;

public class LayoutParams {
    private final LoginStateModel login;
    private final Consumer<Room> onRoomChange;
    private Room room;

    public LayoutParams(LoginStateModel login, Room room, Consumer<Room> onRoomChange) {
        this.login = login;
        this.room = room;
        this.onRoomChange = onRoomChange;
    }

    public LoginStateModel getLogin() {
        return this.login;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
        onRoomChange.accept(room);
    }
}
