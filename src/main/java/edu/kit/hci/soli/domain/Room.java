package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.Data;

/**
 * The datamodel for a room as it is stored in the database
 */
@Entity
@Table(name = "soli_rooms")
@Data
public class Room {
    /**
     * The unique identifier for the room.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
