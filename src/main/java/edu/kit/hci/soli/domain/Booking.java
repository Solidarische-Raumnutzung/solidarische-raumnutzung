package edu.kit.hci.soli.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The datamodel for a Booking as it is stored in the database.
 */
@Entity
@Table(name = "soli_bookings")
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

    public Booking(Long id, String description, LocalDateTime startDate, LocalDateTime endDate, ShareRoomType shareRoomType, Room room, User user, Priority priority, Set<User> outstandingRequests) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shareRoomType = shareRoomType;
        this.room = room;
        this.user = user;
        this.priority = priority;
        this.outstandingRequests = outstandingRequests;
    }

    public Booking() {
    }

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public ShareRoomType getShareRoomType() {
        return this.shareRoomType;
    }

    public Room getRoom() {
        return this.room;
    }

    public User getUser() {
        return this.user;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Set<User> getOutstandingRequests() {
        return this.outstandingRequests;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setShareRoomType(ShareRoomType shareRoomType) {
        this.shareRoomType = shareRoomType;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setOutstandingRequests(Set<User> outstandingRequests) {
        this.outstandingRequests = outstandingRequests;
    }

    public String toString() {
        return "Booking(id=" + this.getId() + ", description=" + this.getDescription() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", shareRoomType=" + this.getShareRoomType() + ", room=" + this.getRoom() + ", user=" + this.getUser() + ", priority=" + this.getPriority() + ", outstandingRequests=" + this.getOutstandingRequests() + ")";
    }
}
