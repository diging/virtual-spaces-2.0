package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

public interface ISpaceLinkManager extends ILinkManager<ISpaceLink,ISpace,ISpaceLinkDisplay>{

    List<ISpaceLinkDisplay> getSpaceLinkForGivenOrNullSpaceStatus(String spaceId, SpaceStatus spaceStatus);
}
