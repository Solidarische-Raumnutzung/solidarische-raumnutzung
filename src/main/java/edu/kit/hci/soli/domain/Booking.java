package edu.kit.hci.soli.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The datamodel for a Booking as it is stored in the database
 */
@Entity(name = "tbl_booking")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ShareRoomType shareRoomType;

    @OneToOne
    private User user;

    private Priority priority;

}
