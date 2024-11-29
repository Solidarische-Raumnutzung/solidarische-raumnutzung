package edu.kit.hci.soli.domain;

import edu.kit.hci.soli.controller.BookingsController;
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
