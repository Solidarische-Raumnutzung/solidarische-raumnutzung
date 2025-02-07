package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.repository.SystemConfigurationRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.SystemConfigurationService;
import edu.kit.hci.soli.service.TimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {
    private final SystemConfigurationRepository systemConfigurationRepository;
    private final BookingsRepository bookingsRepository;
    private final BookingsService bookingsService;
    private final TimeService timeService;

    /**
     * Constructs a SystemConfigurationService with the specified {@link SystemConfigurationRepository}.
     *
     * @param systemConfigurationRepository the repository for managing the configuration
     * @param bookingsRepository            the repository for managing bookings
     * @param bookingsService               the service for managing bookings
     * @param timeService                   the service for managing time
     */
    public SystemConfigurationServiceImpl(SystemConfigurationRepository systemConfigurationRepository, BookingsRepository bookingsRepository, BookingsService bookingsService, TimeService timeService) {
        this.systemConfigurationRepository = systemConfigurationRepository;
        this.bookingsRepository = bookingsRepository;
        this.bookingsService = bookingsService;
        this.timeService = timeService;
    }

    @Override
    public boolean isGuestLoginEnabled() {
        return systemConfigurationRepository.getInstance().isGuestLoginEnabled();
    }

    @Override
    @Transactional
    public void setGuestLoginEnabled(boolean enabled) {
        systemConfigurationRepository.getInstance().setGuestLoginEnabled(enabled);
        if (!enabled) {
            try (var bookings = bookingsRepository.findFutureBookingsByGuests(timeService.now())) {
                bookings.forEach(b -> bookingsService.delete(b, BookingDeleteReason.GUESTS_DEACTIVATED));
            }
        }
    }
}
