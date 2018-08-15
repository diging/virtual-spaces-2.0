package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISpace extends IVSpaceElement {

	List<ISpaceLink> getSpaceLinks();

	void setSpaceLinks(List<ISpaceLink> spaceLinks);

	List<IModuleLink> getModuleLinks();

	void setModuleLinks(List<IModuleLink> moduleLinks);

	void setImage(IVSImage image);

	IVSImage getImage();

}