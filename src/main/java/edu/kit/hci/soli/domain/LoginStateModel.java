package edu.kit.hci.soli.domain;

import org.springframework.lang.Nullable;
import org.springframework.security.web.csrf.CsrfToken;

public record LoginStateModel(String name, Kind kind, CsrfToken csrfToken, @Nullable User user) {
    public enum Kind {
        VISITOR,
        OAUTH,
        //        GUEST,
        ADMIN
    }
}
