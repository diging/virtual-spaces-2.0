package edu.asu.diging.vspace.core.model;

public enum SortByField {
    CREATION_DATE("creationDate");

    private final String value;

    private SortByField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
