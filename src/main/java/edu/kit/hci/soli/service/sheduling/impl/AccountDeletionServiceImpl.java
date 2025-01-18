package edu.kit.hci.soli.service.sheduling.impl;

import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.service.sheduling.AccountsDeletionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AccountDeletionServiceImpl implements AccountsDeletionService {
    private final UserRepository userRepository;

    /**
     * Constructor for the account deletion service implementation.
     *
     * @param userRepository the userRepository to use
     */
    public AccountDeletionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAccountOlderThanThreeMonths() {

    }
}
