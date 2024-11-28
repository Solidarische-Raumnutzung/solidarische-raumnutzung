package edu.kit.hci.soli.domain;

import org.springframework.security.web.csrf.CsrfToken;

public class LoginStateModel {
    public String name;
    public long visits;
    public Kind kind;
    public CsrfToken csrfToken;


    public LoginStateModel(String name, long visits, Kind kind, CsrfToken csrfToken) {
        this.name = name;
        this.visits = visits;
        this.kind = kind;
        this.csrfToken = csrfToken;
    }

    public enum Kind {
        VISITOR,
        OAUTH,
//        GUEST,
        ADMIN
    }
}
