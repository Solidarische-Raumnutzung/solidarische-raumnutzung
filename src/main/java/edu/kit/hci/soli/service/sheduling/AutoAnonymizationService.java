package edu.kit.hci.soli.service.sheduling;

/**
 * Service interface for deleting accounts on a day-to-day basis.
 */
public interface AutoAnonymizationService {
    /**
     * Deletes all accounts that have not been used for more than three months.
     *
     * Anonymizes all bookings older than three months.
     */
    void scheduledAnonymize();
}
