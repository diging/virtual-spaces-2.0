package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISpace;

public interface ISpaceManager {

	ISpace storeSpace(ISpace space, String username);

}