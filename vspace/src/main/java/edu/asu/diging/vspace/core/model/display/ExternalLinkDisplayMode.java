package edu.asu.diging.vspace.core.model.display;

public enum ExternalLinkDisplayMode {

    NEWWINDOW("NEWWINDOW"),SAMEWINDOW("SAMEWINDOW");
    
    private final String value;

    private ExternalLinkDisplayMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
