package edu.asu.diging.vspace.core.model;

public enum IdPrefix {

    SPACEID(IPrefix.SPACE_PREFIX),
    MODULEID(IPrefix.MODULE_PREFIX);

    private final String value;

    private IdPrefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
