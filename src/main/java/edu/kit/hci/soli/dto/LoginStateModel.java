package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.web.csrf.CsrfToken;

public record LoginStateModel(String name, Kind kind, CsrfToken csrfToken, @Nullable User user) {
    public enum Kind {
        VISITOR,
        OAUTH,
//        GUEST,
        ADMIN
    }
}
