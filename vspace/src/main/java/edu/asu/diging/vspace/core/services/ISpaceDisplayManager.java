package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;

public interface ISpaceDisplayManager {

    ISpaceDisplay getBySpace(ISpace space);

}