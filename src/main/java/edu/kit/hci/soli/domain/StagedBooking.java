package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "soli_bookings_staged")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StagedBooking {
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
    private Set<User> outstandingRequests;

    public Booking toBooking() {
        return new Booking(null, description, startDate, endDate, shareRoomType, room, user, priority);
    }
}
