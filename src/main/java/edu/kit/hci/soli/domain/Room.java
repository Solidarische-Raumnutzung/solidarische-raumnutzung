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

    private String location;

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

    /**
     * Get the description for the location of the room.
     *
     * @return the location description
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the description for the location of the room.
     *
     * @param location the new location for the room
     */
    public void setLocation(String location) {
        this.location = location;
    }

    public boolean equals(final Object o) {
        return o instanceof Room r && getId().equals(r.getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }
}
