package edu.asu.diging.vspace.core.model;

public interface ISpaceLink extends ILink<ISpace> {

	ISpace getSourceSpace();

	void setSourceSpace(ISpace sourceSpace);

	ISpace getTargetSpace();

	void setTargetSpace(ISpace targetSpace);
	
	// void setHideLinks(int hideLinks);
}