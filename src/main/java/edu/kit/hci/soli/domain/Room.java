package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.ToString;

/**
 * The datamodel for a room as it is stored in the database.
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

    private String name;

    private String description;

    /**
     * Constructs a new room with the specified details.
     *
     * @param id the unique identifier for the room
     * @param name the name of the room
     * @param description the description of the room
     */
    public Room(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Default constructor for JPA.
     */
    public Room() {
    }

    /**
     * Gets the unique identifier for the room.
     *
     * @return the unique identifier for the room
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Gets the name of the room.
     *
     * @return the name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the room.
     *
     * @return the description of the room
     */
    public String getDescription() {
        return description;
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
     * Sets the name of the room.
     *
     * @param name the name of the room
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description for the room.
     *
     * @param description the description of the room
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return o instanceof Room r && getId().equals(r.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
