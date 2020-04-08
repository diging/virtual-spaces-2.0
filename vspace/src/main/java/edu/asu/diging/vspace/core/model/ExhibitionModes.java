package edu.asu.diging.vspace.core.model;

import java.util.ArrayList;
import java.util.List;

public enum ExhibitionModes {

    ACTIVE("Active"), MAINTENANCE("Maintenance"), OFFLINE("Offline");

    private final String value;

    private ExhibitionModes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getAllValues() {
        List<String> valuesList = new ArrayList<>();
        for(ExhibitionModes val : ExhibitionModes.values()) {
            valuesList.add(val.getValue());
        }
        return valuesList;
    }
}
