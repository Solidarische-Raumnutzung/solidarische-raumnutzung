package edu.kit.hci.soli.domain;

import edu.kit.hci.soli.controller.BookingsController;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

/**
 * The datamodel for a room as it is stored in the database
 */
@Entity
@Data
public class Room {

    @Id
    private Long id;

    @OneToOne
    private Booking currentBooking;

}
