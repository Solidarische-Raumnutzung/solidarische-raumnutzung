package edu.kit.hci.soli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kit.hci.soli.domain.User;
import java.io.Serializable;

public interface UserRepository extends JpaRepository<User, Serializable> {

}
