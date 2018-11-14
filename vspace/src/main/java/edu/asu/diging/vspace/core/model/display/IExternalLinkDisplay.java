package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.IExternalLink;

public interface IExternalLinkDisplay extends IDisplay {

    String getId();

    void setId(String id);

    void setExternalLink(IExternalLink link);

    IExternalLink getExternalLink();
}
