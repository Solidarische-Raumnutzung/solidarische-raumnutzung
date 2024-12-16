package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.web.csrf.CsrfToken;

/**
 * Record representing the login state model.
 *
 * @param name      the name of the user
 * @param kind      the kind of login
 * @param csrfToken the CSRF token associated with the login
 * @param user      the user associated with the login, can be null
 */
public record LoginStateModel(String name, Kind kind, CsrfToken csrfToken, @Nullable User user) {
    /**
     * Enumeration representing the kind of login.
     */
    public enum Kind {
        /**
         * The user hasn't logged in yet.
         */
        VISITOR,

        /**
         * The user has logged in via OAuth.
         */
        OAUTH,

        /**
         * The user has logged in as a guest.
         */
        GUEST,

        /**
         * The user has logged in as an administrator.
         */
        ADMIN
    }
}
