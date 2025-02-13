package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.SystemConfiguration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * Repository interface for managing the system configuration.
 * This repository provides a method to retrieve the single instance of {@link SystemConfiguration}.
 */
public interface SystemConfigurationRepository extends Repository<SystemConfiguration, Integer> {
    /**
     * Retrieves the single instance of {@link SystemConfiguration}.
     *
     * @return the {@link SystemConfiguration} instance.
     */
    @Query("SELECT s FROM SystemConfiguration s")
    SystemConfiguration getInstance();
}
