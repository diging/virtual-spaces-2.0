package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

public interface ISpace extends IVSpaceElement {

	List<SpaceLink> getSpaceLinks();

	void setSpaceLinks(List<SpaceLink> spaceLinks);

	List<ModuleLink> getModuleLinks();

	void setModuleLinks(List<ModuleLink> moduleLinks);

	void setImage(IVSImage image);

	IVSImage getImage();

}