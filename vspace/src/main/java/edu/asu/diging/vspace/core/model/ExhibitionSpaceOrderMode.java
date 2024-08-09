package edu.asu.diging.vspace.core.model;

import java.util.ArrayList;
import java.util.List;

public enum ExhibitionSpaceOrderMode {
    ALPHABETICAL("alphabetical"),
    CREATION_DATE("creationDate"),
    CUSTOM("custom");
    
    private final String value;

    private ExhibitionSpaceOrderMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static List<String> getAllValues() {
        List<String> allValues = new ArrayList<>();
        for(ExhibitionSpaceOrderMode order : ExhibitionSpaceOrderMode.values()) {
            allValues.add(order.getValue());
        }
        return allValues;
    }
}
