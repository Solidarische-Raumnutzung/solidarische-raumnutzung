package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.ToString;

/**
 * The datamodel for a room as it is stored in the database
 */
@Entity
@Table(name = "soli_rooms")
@ToString
public class Room {
    /**
     * The unique identifier for the room.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Gets the unique identifier for the room.
     *
     * @return the unique identifier for the room
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the unique identifier for the room.
     *
     * @param id the unique identifier for the room
     */
    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(final Object o) {
        return o instanceof Room r && getId().equals(r.getId());
    }

    public int hashCode() {
       return getId().hashCode();
    }
}
