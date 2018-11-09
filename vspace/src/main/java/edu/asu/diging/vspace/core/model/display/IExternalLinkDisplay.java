package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface IExternalLinkDisplay extends IVSpaceElement {

    String getId();

    void setId(String id);

    float getPositionX();

    void setPositionX(float positionX);

    float getPositionY();

    void setPositionY(float positionY);

    void setExternalLink(IExternalLink link);

    IExternalLink getExternalLink();
}
