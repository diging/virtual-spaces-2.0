package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface ISpaceManager {

	CreationReturnValue storeSpace(ISpace space, String username, byte[] image, String filename);

	ISpace getSpace(String id);

}