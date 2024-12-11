package edu.kit.hci.soli.domain;

import jakarta.persistence.*;

/**
 * The datamodel for a room as it is stored in the database
 */
@Entity
@Table(name = "soli_rooms")
public class Room {
    /**
     * The unique identifier for the room.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Room() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return "Room(id=" + this.getId() + ")";
    }
}
