package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    @Nullable User findByUserId(String userId);

    @NotNull User create(User user);

    void toggleUserEnabled(User user);

    @NotNull List<User> getManageableUsers();

    @Nullable User getById(Long userId);

    @NotNull User resolveOidcUser(OidcUser oidcUser);

    @NotNull User resolveAdminUser();

    boolean isAdmin(User user);

    @NotNull User resolveGuestUser(String email);

    boolean isGuestEnabled();

    boolean isGuest(User user);
}
