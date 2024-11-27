package edu.kit.hci.soli.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The datamodel for a Booking as it is stored in the database
 */
@Entity
@Data
public class Booking {

    @Id
    private Long id;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ShareRoomType shareRoomType;

    @ManyToOne
    private User user;

    private Priority priority;

}
