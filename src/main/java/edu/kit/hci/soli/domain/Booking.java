package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The datamodel for a Booking as it is stored in the database.
 */
@Entity
@Table(name = "soli_bookings")
@ToString
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

    /**
     * Constructs a new Booking with the specified details.
     *
     * @param id                  the unique identifier for the booking
     * @param description         a description of the booking
     * @param startDate           the start date and time of the booking
     * @param endDate             the end date and time of the booking
     * @param shareRoomType       the kind of room sharing for the booking
     * @param room                the room associated with the booking
     * @param user                the user who made the booking
     * @param priority            the priority level of the booking
     * @param outstandingRequests the set of share requests that must still be resolved
     */
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

    /**
     * Default constructor for Booking.
     */
    public Booking() {
    }

    /**
     * Gets the unique identifier for the booking.
     *
     * @return the unique identifier for the booking
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Gets the description of the booking.
     *
     * @return the description of the booking
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the start date and time of the booking.
     *
     * @return the start date and time of the booking
     */
    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    /**
     * Gets the end date and time of the booking.
     *
     * @return the end date and time of the booking
     */
    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    /**
     * Gets the kind of room sharing for the booking.
     *
     * @return the kind of room sharing for the booking
     */
    public ShareRoomType getShareRoomType() {
        return this.shareRoomType;
    }

    /**
     * Gets the room associated with the booking.
     *
     * @return the room associated with the booking
     */
    public Room getRoom() {
        return this.room;
    }

    /**
     * Gets the user who made the booking.
     *
     * @return the user who made the booking
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Gets the priority level of the booking.
     *
     * @return the priority level of the booking
     */
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * Gets the set of share requests that must still be resolved for this booking to be confirmed.
     *
     * @return the set of share requests that must still be resolved
     */
    public Set<User> getOutstandingRequests() {
        return this.outstandingRequests;
    }

    /**
     * Sets the unique identifier for the booking.
     *
     * @param id the unique identifier for the booking
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the description of the booking.
     *
     * @param description the description of the booking
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the start date and time of the booking.
     *
     * @param startDate the start date and time of the booking
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets the end date and time of the booking.
     *
     * @param endDate the end date and time of the booking
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Sets the kind of room sharing for the booking.
     *
     * @param shareRoomType the kind of room sharing for the booking
     */
    public void setShareRoomType(ShareRoomType shareRoomType) {
        this.shareRoomType = shareRoomType;
    }

    /**
     * Sets the room associated with the booking.
     *
     * @param room the room associated with the booking
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Sets the user who made the booking.
     *
     * @param user the user who made the booking
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the priority level of the booking.
     *
     * @param priority the priority level of the booking
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Sets the set of share requests that must still be resolved for this booking to be confirmed.
     *
     * @param outstandingRequests the set of share requests that must still be resolved
     */
    public void setOutstandingRequests(Set<User> outstandingRequests) {
        this.outstandingRequests = outstandingRequests;
    }

    public boolean equals(final Object o) {
        if (getId() == null) {
            return false;
        }
        return o instanceof Booking b && getId().equals(b.getId());
    }

    public int hashCode() {
        if (getId() == null) {
            return 0;
        }
        return getId().hashCode();
    }
}
