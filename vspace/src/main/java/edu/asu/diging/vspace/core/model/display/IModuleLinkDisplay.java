package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpaceLink;

public interface IModuleLinkDisplay extends ILinkDisplay{
	String getId();

    void setId(String id);

    void setLink(IModuleLink link);

    IModuleLink getLink();

    void setRotation(int rotation);

    int getRotation();

    void setType(DisplayType type);

    DisplayType getType();
}
