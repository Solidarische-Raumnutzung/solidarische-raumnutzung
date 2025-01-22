package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

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

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomOpeningHours> openingHours;

    @ElementCollection
    @CollectionTable(name = "room_opening_hours",
            joinColumns = {@JoinColumn(name = "room_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "day_of_week")
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "start_time")),
            @AttributeOverride(name = "end", column = @Column(name = "end_time"))
    })
    private Map<DayOfWeek, TimeTuple> openingHoursN;

    @Data
    @Embeddable
    public static class TimeTuple {
        private LocalTime start;
        private LocalTime end;
    }

    private String name;

    private String description;

    private String location;

    /**
     * Constructs a new room with the specified details.
     *
     * @param id the unique identifier for the room
     * @param name the name of the room
     * @param description the description of the room
     */
    public Room(Long id, String name, String description, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
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

    public Set<RoomOpeningHours> getOpeningHours() {
        return this.openingHours;
    }
}
