package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.Data;

/**
 * The datamodel for a room as it is stored in the database
 */
@Entity
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
