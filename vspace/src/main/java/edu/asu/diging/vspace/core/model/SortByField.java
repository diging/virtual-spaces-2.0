package edu.asu.diging.vspace.core.model;

import java.util.ArrayList;
import java.util.List;

public enum SortByField {
    CREATION_DATE("creationDate"), FILENAME("filename"), NAME("name"), CREATED_BY("createdBy"), 
    REFERENCE_TITLE("title"), REFERENCE_AUTHOR("author"), REFERENCE_YEAR("year"), REFERENCE_JOURNAL("journal");

    private final String value;

    private SortByField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getAllValues() {
        List<String> allValues = new ArrayList<>();
        for(SortByField sbf : SortByField.values()) {
            allValues.add(sbf.getValue());
        }
        return allValues;
    }
}
