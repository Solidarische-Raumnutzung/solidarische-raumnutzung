package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The datamodel for a Booking as it is stored in the database.
 */
@Entity
@Table(name = "soli_bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    /**
     * The internal unique identifier for the booking.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * A description of the booking. This is displayed for logged-in users.
     */
    private String description;

    /**
     * The start date and time of the booking.
     */
    private LocalDateTime startDate;

    /**
     * The end date and time of the booking.
     */
    private LocalDateTime endDate;

    /**
     * The kind of room sharing for the booking.
     */
    private ShareRoomType shareRoomType;

    /**
     * The room associated with the booking.
     */
    @ManyToOne
    private Room room;

    /**
     * The user who made the booking.
     */
    @ManyToOne
    private User user;

    /**
     * The priority level of the booking.
     */
    private Priority priority;

    /**
     * The set of share requests that must still be resolved for this booking to be confirmed.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "soli_outstanding_requests")
    private Set<User> outstandingRequests = new HashSet<>();
}
