package edu.asu.diging.vspace.core.model;

public enum IdPrefix {

    SPACEID("SPA"),
    MODULEID("MOD");

    private final String value;

    private IdPrefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
