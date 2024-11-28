package edu.kit.hci.soli.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The datamodel for a User as it is stored in the database
 */
@Entity
@Data
public class User {
    @Id
    private Long id;

    private  String username;

    private  String email;

}
