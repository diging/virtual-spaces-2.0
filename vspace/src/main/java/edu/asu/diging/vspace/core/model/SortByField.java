package edu.asu.diging.vspace.core.model;

public enum SortByField {
    CREATION_DATE("creationDate"),
    CREATION_BY("createdBy"),
    FILENAME("filename"),
    NAME("name");
    private final String value;

    private SortByField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
