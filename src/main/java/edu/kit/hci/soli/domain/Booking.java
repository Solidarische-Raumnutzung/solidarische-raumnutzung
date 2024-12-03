package edu.kit.hci.soli.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The datamodel for a Booking as it is stored in the database
 */
@Entity
@Table(name = "soli_bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private ShareRoomType shareRoomType;

    @ManyToOne
    private Room room;

    @ManyToOne
    private User user;

    private Priority priority;

    @ManyToMany
    @JoinTable(name = "soli_outstanding_requests")
    private Set<User> outstandingRequests = new HashSet<>();
}
