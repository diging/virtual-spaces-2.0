package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface ISpaceManager {

	CreationReturnValue storeSpace(ISpace space, byte[] image, String filename);

	ISpace getSpace(String id);

	ISpaceLinkDisplay createSpaceLink(String title, ISpace source, float positionX, float positionY);

}