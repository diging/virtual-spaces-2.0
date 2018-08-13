package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;

public interface ISpaceLinkDisplayFactory {

	ISpaceLinkDisplay createSpaceLinkDisplay(ISpaceLink link);

}