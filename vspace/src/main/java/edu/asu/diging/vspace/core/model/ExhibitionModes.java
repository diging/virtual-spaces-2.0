package edu.asu.diging.vspace.core.model;

public enum ExhibitionModes {

    ACTIVE(""),
    MAINTENANCE("This exhibition is currently under maintenance. Please check back later."),
    OFFLINE("This exhibition is currently offline. Please check back later.");

    private final String value;

    private ExhibitionModes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
