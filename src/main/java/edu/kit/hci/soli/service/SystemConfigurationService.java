package edu.kit.hci.soli.service;

/**
 * Service interface for managing the shared system configuration.
 */
public interface SystemConfigurationService {
    /**
     * Checks if guest login is enabled.
     *
     * @return true if guest login is enabled, false otherwise.
     */
    boolean isGuestLoginEnabled();

    /**
     * Sets the guest login status.
     *
     * @param enabled true to enable guest login, false to disable it.
     */
    void setGuestLoginEnabled(boolean enabled);
}
