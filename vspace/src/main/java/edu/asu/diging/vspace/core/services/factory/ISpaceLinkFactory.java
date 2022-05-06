package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;

public interface ISpaceLinkFactory {

    ISpaceLink createSpaceLink(String title, ISpace source);

}