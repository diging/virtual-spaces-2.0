package edu.asu.diging.vspace.core.model.impl;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

public class ExternalLinkValue extends VSpaceElement implements IVSpaceElement{

    private String value;

    public ExternalLinkValue() {}

    public ExternalLinkValue(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

}
