package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.repository.SystemConfigurationRepository;
import edu.kit.hci.soli.service.SystemConfigurationService;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {
    private final SystemConfigurationRepository systemConfigurationRepository;

    /**
     * Constructs a SystemConfigurationService with the specified {@link SystemConfigurationRepository}.
     *
     * @param systemConfigurationRepository the repository for managing the configuration
     */
    public SystemConfigurationServiceImpl(SystemConfigurationRepository systemConfigurationRepository) {
        this.systemConfigurationRepository = systemConfigurationRepository;
    }

    @Override
    public boolean isGuestLoginEnabled() {
        return systemConfigurationRepository.getInstance().isGuestLoginEnabled();
    }

    @Override
    public void setGuestLoginEnabled(boolean enabled) {
        systemConfigurationRepository.getInstance().setGuestLoginEnabled(enabled);
    }
}
