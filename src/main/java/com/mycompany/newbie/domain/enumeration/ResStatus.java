package com.mycompany.newbie.domain.enumeration;

/**
 * The ResStatus enumeration.
 */
public enum ResStatus {
    SH("Shell"),
    OF("Option"),
    BK("Booked"),
    CL("Cl"),
    TM("Tm"),
    CX("Canceled"),
    CT("Ct"),
    QT("Quoted"),
    WL("Waiting");

    private final String value;

    ResStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
