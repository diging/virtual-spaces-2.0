package edu.asu.diging.vspace.core.model;

public enum ExhibitionLanguageCodes {
    
    EN("en"),
    DE("de"),
    IT("it");
    
    private ExhibitionLanguageCodes(String value) {
        this.value = value;
    }


    private final String value;
    
    
    public String getValue() {
        return value;
    }

}
