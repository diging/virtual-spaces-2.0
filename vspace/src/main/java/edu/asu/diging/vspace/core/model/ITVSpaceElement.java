package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface ITVSpaceElement<T extends IVSpaceElement> extends IVSpaceElement{

    ExternalLinkValue getValue();

}
