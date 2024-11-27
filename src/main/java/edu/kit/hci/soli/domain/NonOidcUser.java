package edu.kit.hci.soli.domain;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class NonOidcUser extends User{

    private String password;
}
